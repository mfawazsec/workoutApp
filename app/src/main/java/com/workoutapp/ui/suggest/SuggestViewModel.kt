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
        db.progressionLogDao()
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

        val recentGroupIds = recentSessions
            .flatMap { it.muscleGroupIds.split(",").filter { id -> id.isNotBlank() } }

        val lastTrained = recentGroupIds
            .groupBy { it }
            .maxByOrNull { it.value.size }
            ?.key

        val (suggestion, reason) = when (lastTrained) {
            "push" -> Pair(
                listOf(MuscleGroup.PULL, MuscleGroup.CORE_SHOULDERS),
                "You last trained PUSH. Time to balance with PULL and CORE."
            )
            "pull" -> Pair(
                listOf(MuscleGroup.LEGS, MuscleGroup.CORE_SHOULDERS),
                "You last trained PULL. Hit LEGS and CORE today."
            )
            "legs" -> Pair(
                listOf(MuscleGroup.PUSH, MuscleGroup.CORE_SHOULDERS),
                "Legs day done. Time for upper body — PUSH and CORE."
            )
            "core_shoulders" -> Pair(
                listOf(MuscleGroup.PUSH, MuscleGroup.LEGS),
                "Core work done. Balance with PUSH and LEGS today."
            )
            else -> Pair(
                listOf(MuscleGroup.PUSH, MuscleGroup.CORE_SHOULDERS),
                "No recent sessions found. Starting fresh with PUSH and CORE."
            )
        }
        _suggestedGroups.value = suggestion
        _reason.value = reason
    }
}
