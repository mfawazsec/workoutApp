package com.workoutapp.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
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

data class ExerciseUiState(
    val exercise: Exercise,
    val weightKg: Float,
    val reps: Int,
    val estimatedOneRepMaxKg: Float,
    val overloadLog: OverloadLog = OverloadLog()
)

class WorkoutViewModel(
    application: Application,
    val muscleGroupIds: List<String>
) : AndroidViewModel(application) {

    private val db = WorkoutDatabase.getInstance(application)
    private val exerciseRepo = ExerciseRepository(db.exerciseDao())
    private val workoutRepo = WorkoutRepository(
        db.workoutSessionDao(),
        db.userExerciseStateDao(),
        db.progressionLogDao()
    )

    private val sessionId = UUID.randomUUID().toString()
    val muscleGroups: List<MuscleGroup> = MuscleGroup.fromIds(muscleGroupIds)

    private val _exercises = MutableStateFlow<List<ExerciseUiState>>(emptyList())
    val exercises: StateFlow<List<ExerciseUiState>> = _exercises.asStateFlow()

    // Full exercise bank per group (for swap sheet)
    private val _allExercisesByGroup = MutableStateFlow<Map<String, List<Exercise>>>(emptyMap())

    init {
        viewModelScope.launch {
            loadExercises()
            saveSessionStart()
        }
    }

    private suspend fun loadExercises() {
        val exercisesPerGroup = mutableMapOf<String, List<Exercise>>()
        val sessionExercises = mutableListOf<ExerciseUiState>()

        for (groupId in muscleGroupIds) {
            val groupExercises = exerciseRepo.getExercisesByGroupSync(groupId)
            exercisesPerGroup[groupId] = groupExercises

            // Pick first 3 exercises for the session
            val selected = groupExercises.take(3)
            for (ex in selected) {
                val savedState = workoutRepo.getExerciseState(ex.id)
                val weightKg = savedState?.currentWeightKg ?: ex.weightKg
                val reps = savedState?.currentReps ?: ex.reps
                val oneRepMax = savedState?.estimatedOneRepMaxKg
                    ?: RepMaxCalculator.estimateOneRepMax(weightKg, reps)

                sessionExercises.add(
                    ExerciseUiState(
                        exercise = ex,
                        weightKg = weightKg,
                        reps = reps,
                        estimatedOneRepMaxKg = oneRepMax
                    )
                )
            }
        }
        _allExercisesByGroup.value = exercisesPerGroup
        _exercises.value = sessionExercises
    }

    private suspend fun saveSessionStart() {
        val exerciseIds = _exercises.value.joinToString(",") { it.exercise.id }
        workoutRepo.saveSession(
            WorkoutSessionEntity(
                sessionId = sessionId,
                dateEpochDay = LocalDate.now().toEpochDay(),
                muscleGroupIds = muscleGroupIds.joinToString(","),
                exerciseIds = exerciseIds,
                completedAt = null
            )
        )
    }

    fun getAlternatives(exercise: Exercise): List<Exercise> {
        val allInGroup = _allExercisesByGroup.value[exercise.muscleGroupId] ?: emptyList()
        val currentIds = _exercises.value.map { it.exercise.id }.toSet()
        return allInGroup.filter { it.id !in currentIds }
    }

    fun swapExercise(old: Exercise, replacement: Exercise) {
        viewModelScope.launch {
            val savedState = workoutRepo.getExerciseState(replacement.id)
            val weightKg = savedState?.currentWeightKg ?: replacement.weightKg
            val reps = savedState?.currentReps ?: replacement.reps
            val oneRepMax = savedState?.estimatedOneRepMaxKg
                ?: RepMaxCalculator.estimateOneRepMax(weightKg, reps)

            _exercises.value = _exercises.value.map { state ->
                if (state.exercise.id == old.id) {
                    ExerciseUiState(
                        exercise = replacement,
                        weightKg = weightKg,
                        reps = reps,
                        estimatedOneRepMaxKg = oneRepMax
                    )
                } else state
            }
        }
    }

    fun updateWeight(exerciseId: String, newWeightKg: Float, suggestedReps: Int) {
        _exercises.value = _exercises.value.map { state ->
            if (state.exercise.id == exerciseId) {
                val newMax = RepMaxCalculator.estimateOneRepMax(newWeightKg, suggestedReps)
                state.copy(
                    weightKg = newWeightKg,
                    reps = suggestedReps,
                    estimatedOneRepMaxKg = newMax
                )
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
                // Also suggest new weight if current weight is non-zero
                val suggestedWeight = if (newMax > 0f)
                    RepMaxCalculator.suggestWeight(newMax, newReps)
                else state.weightKg
                state.copy(reps = newReps, estimatedOneRepMaxKg = newMax)
            } else state
        }
        viewModelScope.launch {
            val state = _exercises.value.find { it.exercise.id == exerciseId } ?: return@launch
            workoutRepo.saveExerciseState(exerciseId, state.weightKg, newReps, state.exercise.sets)
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
