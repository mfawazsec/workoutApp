package com.workoutapp.ui.suggest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class SuggestViewModel(application: Application) : AndroidViewModel(application) {
    private val db = WorkoutDatabase.getInstance(application)
    private val workoutRepo = WorkoutRepository(
        db.workoutSessionDao(),
        db.userExerciseStateDao(),
        db.progressionLogDao(),
        db.exerciseSnapshotDao()
    )

    private val _suggestedGroups = MutableStateFlow<List<MuscleGroup>>(emptyList())
    val suggestedGroups: StateFlow<List<MuscleGroup>> = _suggestedGroups.asStateFlow()

    private val _reason = MutableStateFlow("Analysing your workout history...")
    val reason: StateFlow<String> = _reason.asStateFlow()

    init {
        viewModelScope.launch { loadSuggestion() }
    }

    private suspend fun loadSuggestion() {
        val sevenDaysAgo = LocalDate.now().minusDays(7).toEpochDay()
        val recentSessions = workoutRepo.getSessionsSince(sevenDaysAgo)

        val lastTrained = recentSessions
            .maxByOrNull { it.startedAtMs }
            ?.muscleGroupIds
            ?.split(",")
            ?.firstOrNull { it.isNotBlank() }

        val (suggestion, reason) = when (lastTrained) {
            "push" -> Pair(
                listOf(MuscleGroup.PULL),
                "You last trained PUSH. Time for PULL."
            )
            "pull" -> Pair(
                listOf(MuscleGroup.LEGS),
                "You last trained PULL. Hit LEGS today."
            )
            "legs" -> Pair(
                listOf(MuscleGroup.CORE_SHOULDERS),
                "Legs day done. Time for CORE + SHOULDERS."
            )
            "core_shoulders" -> Pair(
                listOf(MuscleGroup.PUSH),
                "Core work done. Time for PUSH."
            )
            else -> Pair(
                listOf(MuscleGroup.PUSH),
                "No recent sessions found. Starting fresh with PUSH."
            )
        }
        _suggestedGroups.value = suggestion
        _reason.value = reason
    }
}
