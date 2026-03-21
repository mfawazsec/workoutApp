package com.workoutapp.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import com.workoutapp.data.entity.ProgressionLogEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import com.workoutapp.data.model.Exercise
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.data.repository.ExerciseRepository
import com.workoutapp.data.repository.WorkoutRepository
import com.workoutapp.data.util.RepMaxCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

private const val TARGET_WORKOUT_MINUTES = 75.0
private const val MIN_EXERCISES_PER_GROUP = 3
private const val MAX_EXERCISES_PER_GROUP = 4
private const val TRANSITION_SECONDS = 60.0

data class ExerciseUiState(
    val exercise: Exercise,
    val weightKg: Float,
    val reps: Int,
    val estimatedOneRepMaxKg: Float,
    val overloadLog: OverloadLog = OverloadLog(),
    val completedSets: Int = 0,
    val setTimestamps: List<Long> = emptyList()
) {
    val isDone: Boolean get() = completedSets >= exercise.sets
}

class WorkoutViewModel(
    application: Application,
    val muscleGroupIds: List<String>
) : AndroidViewModel(application) {

    private val db = WorkoutDatabase.getInstance(application)
    private val exerciseRepo = ExerciseRepository(db.exerciseDao())
    private val workoutRepo = WorkoutRepository(
        db.workoutSessionDao(),
        db.userExerciseStateDao(),
        db.progressionLogDao(),
        db.exerciseSnapshotDao()
    )

    private val sessionId = UUID.randomUUID().toString()
    val muscleGroups: List<MuscleGroup> = MuscleGroup.fromIds(muscleGroupIds)

    /** Canonical day key — sorted so push+pull == pull+push */
    private val dayKey = muscleGroupIds.sorted().joinToString(",")

    private val _exercises = MutableStateFlow<List<ExerciseUiState>>(emptyList())
    val exercises: StateFlow<List<ExerciseUiState>> = _exercises.asStateFlow()

    // Full exercise bank per sub-group (for swap sheet and add/remove)
    private val _allExercisesBySubGroup = MutableStateFlow<Map<String, List<Exercise>>>(emptyMap())

    init {
        viewModelScope.launch {
            loadExercises()
            saveSessionStart()
        }
    }

    private suspend fun loadExercises() {
        val exercisesPerSubGroup = mutableMapOf<String, List<Exercise>>()
        val sessionExercises = mutableListOf<ExerciseUiState>()

        // Collect all exercises across all groups
        val allByGroup = mutableMapOf<String, List<Exercise>>()
        for (groupId in muscleGroupIds) {
            val entities = exerciseRepo.getExercisesByGroupSync(groupId)
            allByGroup[groupId] = entities
        }

        // Build sub-group bank
        for ((_, exercises) in allByGroup) {
            for (ex in exercises) {
                val current = exercisesPerSubGroup.getOrDefault(ex.subGroupId, emptyList())
                exercisesPerSubGroup[ex.subGroupId] = current + ex
            }
        }
        _allExercisesBySubGroup.value = exercisesPerSubGroup

        // Check for a saved config from the previous session for this day type
        val prevSession = workoutRepo.getLastCompletedSessionForDayKey(dayKey)
        val prevExerciseIds = prevSession?.exerciseIds
            ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()

        val allExercises = allByGroup.values.flatten()

        if (prevExerciseIds.isNotEmpty()) {
            // Restore previous session exercise selection (preserving original order)
            val prevExercises = allExercises.filter { it.id in prevExerciseIds }
                .sortedWith(compareBy({ muscleGroupIds.indexOf(it.muscleGroupId) }, { it.sortOrder }))
            for (ex in prevExercises) {
                sessionExercises.add(buildUiState(ex))
            }
        } else {
            // First time — auto-select by time budget per sub-group
            val subGroupOrder = allExercises.map { it.subGroupId }.distinct()
            val numSubGroups = subGroupOrder.size
            val budgetMinutes = TARGET_WORKOUT_MINUTES / numSubGroups

            for (subGroupId in subGroupOrder) {
                val groupExercises = exercisesPerSubGroup[subGroupId] ?: continue
                val selected = selectByTimeBudget(groupExercises, budgetMinutes)
                for (ex in selected) {
                    sessionExercises.add(buildUiState(ex))
                }
            }
        }

        _exercises.value = sessionExercises
    }

    private suspend fun buildUiState(ex: Exercise): ExerciseUiState {
        val savedState = workoutRepo.getExerciseState(ex.id)
        val weightKg = savedState?.currentWeightKg ?: ex.weightKg
        val reps = savedState?.currentReps ?: ex.reps
        val oneRepMax = savedState?.estimatedOneRepMaxKg
            ?: RepMaxCalculator.estimateOneRepMax(weightKg, reps)
        return ExerciseUiState(exercise = ex, weightKg = weightKg, reps = reps, estimatedOneRepMaxKg = oneRepMax)
    }

    private suspend fun saveSessionStart() {
        val exerciseIds = _exercises.value.joinToString(",") { it.exercise.id }
        workoutRepo.saveSession(
            WorkoutSessionEntity(
                sessionId = sessionId,
                dateEpochDay = LocalDate.now().toEpochDay(),
                muscleGroupIds = dayKey,
                exerciseIds = exerciseIds,
                startedAtMs = System.currentTimeMillis(),
                completedAt = null
            )
        )
        // Save initial snapshots for this session
        val snapshots = _exercises.value.map { state ->
            ExerciseSnapshotEntity(
                sessionId = sessionId,
                exerciseId = state.exercise.id,
                exerciseName = state.exercise.name,
                sets = state.exercise.sets,
                weightKg = state.weightKg,
                reps = state.reps,
                isTimeBased = state.exercise.isTimeBased
            )
        }
        workoutRepo.saveExerciseSnapshots(snapshots)
    }

    fun getAlternatives(exercise: Exercise): List<Exercise> {
        val allInSubGroup = _allExercisesBySubGroup.value[exercise.subGroupId] ?: emptyList()
        val currentIds = _exercises.value.map { it.exercise.id }.toSet()
        return allInSubGroup.filter { it.id !in currentIds }
    }

    fun hasMoreExercises(subGroupId: String): Boolean {
        val inSession = _exercises.value.map { it.exercise.id }.toSet()
        val currentCount = _exercises.value.count { it.exercise.subGroupId == subGroupId }
        if (currentCount >= MAX_EXERCISES_PER_GROUP) return false
        return (_allExercisesBySubGroup.value[subGroupId] ?: emptyList()).any { it.id !in inSession }
    }

    fun addExercise(subGroupId: String) {
        val currentCount = _exercises.value.count { it.exercise.subGroupId == subGroupId }
        if (currentCount >= MAX_EXERCISES_PER_GROUP) return
        val inSession = _exercises.value.map { it.exercise.id }.toSet()
        val available = (_allExercisesBySubGroup.value[subGroupId] ?: emptyList())
            .firstOrNull { it.id !in inSession } ?: return
        viewModelScope.launch {
            val state = buildUiState(available)
            _exercises.value = _exercises.value + state
            workoutRepo.saveExerciseSnapshots(listOf(
                ExerciseSnapshotEntity(
                    sessionId = sessionId,
                    exerciseId = available.id,
                    exerciseName = available.name,
                    sets = available.sets,
                    weightKg = state.weightKg,
                    reps = state.reps,
                    isTimeBased = available.isTimeBased
                )
            ))
        }
    }

    fun removeExercise(subGroupId: String) {
        val inGroup = _exercises.value.filter { it.exercise.subGroupId == subGroupId }
        if (inGroup.size <= 1) return
        val toRemove = inGroup.last()
        _exercises.value = _exercises.value.filter { it.exercise.id != toRemove.exercise.id }
    }

    fun completeNextSet(exerciseId: String) {
        val current = _exercises.value.find { it.exercise.id == exerciseId } ?: return
        if (current.isDone) return
        val now = System.currentTimeMillis()
        val newTimestamps = current.setTimestamps + now
        val newCount = current.completedSets + 1
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId)
                state.copy(completedSets = newCount, setTimestamps = newTimestamps)
            else state
        }
        val timestampStr = newTimestamps.joinToString(",")
        viewModelScope.launch {
            workoutRepo.updateSetTimestamps(sessionId, exerciseId, timestampStr)
            if (newCount >= current.exercise.sets) {
                val updated = _exercises.value.find { it.exercise.id == exerciseId } ?: return@launch
                workoutRepo.markExerciseDone(
                    sessionId, exerciseId,
                    updated.exercise.sets, updated.weightKg, updated.reps
                )
            }
        }
    }

    fun swapExercise(old: Exercise, replacement: Exercise) {
        viewModelScope.launch {
            val state = buildUiState(replacement)
            _exercises.value = _exercises.value.map { s ->
                if (s.exercise.id == old.id) state else s
            }
        }
    }

    fun updateWeight(exerciseId: String, newWeightKg: Float, suggestedReps: Int) {
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId) {
                val newMax = RepMaxCalculator.estimateOneRepMax(newWeightKg, suggestedReps)
                state.copy(weightKg = newWeightKg, reps = suggestedReps, estimatedOneRepMaxKg = newMax)
            } else state
        }
        viewModelScope.launch {
            val state = _exercises.value.find { it.exercise.id == exerciseId } ?: return@launch
            workoutRepo.saveExerciseState(exerciseId, newWeightKg, suggestedReps, state.exercise.sets)
        }
    }

    fun updateReps(exerciseId: String, newReps: Int) {
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId) {
                val newMax = RepMaxCalculator.estimateOneRepMax(state.weightKg, newReps)
                state.copy(reps = newReps, estimatedOneRepMaxKg = newMax)
            } else state
        }
        viewModelScope.launch {
            val state = _exercises.value.find { it.exercise.id == exerciseId } ?: return@launch
            workoutRepo.saveExerciseState(exerciseId, state.weightKg, newReps, state.exercise.sets)
        }
    }

    fun updateSets(exerciseId: String, newSets: Int) {
        val clamped = newSets.coerceIn(1, 10)
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId) {
                val updatedExercise = state.exercise.copy(sets = clamped)
                val clampedCompleted = minOf(state.completedSets, clamped)
                val trimmedTimestamps = state.setTimestamps.take(clampedCompleted)
                state.copy(
                    exercise = updatedExercise,
                    completedSets = clampedCompleted,
                    setTimestamps = trimmedTimestamps
                )
            } else state
        }
        viewModelScope.launch {
            workoutRepo.updateSnapshotSets(sessionId, exerciseId, clamped)
        }
    }

    fun logProgression(exerciseId: String, addedReps: Int, addedEccentric: Int, reducedRest: Int) {
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId) {
                val log = state.overloadLog
                state.copy(
                    overloadLog = log.copy(
                        addedReps = log.addedReps + addedReps,
                        addedEccentricSeconds = log.addedEccentricSeconds + addedEccentric,
                        reducedRestSeconds = log.reducedRestSeconds + reducedRest
                    )
                )
            } else state
        }
        viewModelScope.launch {
            val state = _exercises.value.find { it.exercise.id == exerciseId } ?: return@launch
            workoutRepo.logProgression(
                ProgressionLogEntity(
                    sessionId = sessionId,
                    exerciseId = exerciseId,
                    performedWeightKg = state.weightKg,
                    performedReps = state.reps,
                    performedSets = state.exercise.sets,
                    addedEccentricSeconds = addedEccentric,
                    reducedRestSeconds = reducedRest,
                    dateEpochDay = LocalDate.now().toEpochDay(),
                    recordedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun completeSession() {
        viewModelScope.launch {
            workoutRepo.completeSession(sessionId)
        }
    }

    // ── Time-budget selection ─────────────────────────────────────────────────

    private fun Exercise.estimatedMinutes(): Double {
        val activeSecsPerSet = if (tempo != null) {
            val secsPerRep = (tempo.eccentric + tempo.bottomPause + tempo.concentric + tempo.topPause)
            reps * secsPerRep.toDouble()
        } else {
            45.0 // isometric hold estimate
        }
        val restTime = (sets - 1) * restSeconds.toDouble()
        return (sets * activeSecsPerSet + restTime + TRANSITION_SECONDS) / 60.0
    }

    private fun selectByTimeBudget(exercises: List<Exercise>, budgetMinutes: Double): List<Exercise> {
        val selected = mutableListOf<Exercise>()
        var cumulativeMinutes = 0.0
        for (ex in exercises) {
            if (selected.size >= MAX_EXERCISES_PER_GROUP) break
            if (selected.size >= MIN_EXERCISES_PER_GROUP && cumulativeMinutes >= budgetMinutes) break
            selected.add(ex)
            cumulativeMinutes += ex.estimatedMinutes()
        }
        return selected
    }

    class Factory(
        private val application: Application,
        private val muscleGroupIds: List<String>
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(application, muscleGroupIds) as T
        }
    }
}
