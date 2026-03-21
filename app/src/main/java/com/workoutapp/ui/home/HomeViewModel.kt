package com.workoutapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.data.repository.ExerciseRepository
import com.workoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = WorkoutDatabase.getInstance(application)
    private val exerciseRepo = ExerciseRepository(db.exerciseDao())
    private val workoutRepo = WorkoutRepository(
        db.workoutSessionDao(),
        db.userExerciseStateDao(),
        db.progressionLogDao(),
        db.exerciseSnapshotDao()
    )

    private val _selectedGroups = MutableStateFlow<Set<MuscleGroup>>(emptySet())
    val selectedGroups: StateFlow<Set<MuscleGroup>> = _selectedGroups.asStateFlow()

    private val _suggestedGroups = MutableStateFlow<List<MuscleGroup>>(emptyList())
    val suggestedGroups: StateFlow<List<MuscleGroup>> = _suggestedGroups.asStateFlow()

    init {
        viewModelScope.launch {
            exerciseRepo.seedIfEmpty()
            loadSuggestion()
        }
    }

    fun toggleGroup(group: MuscleGroup) {
        val current = _selectedGroups.value.toMutableSet()
        if (current.contains(group)) current.remove(group) else current.add(group)
        _selectedGroups.value = current
    }

    fun applySuggestion() {
        _selectedGroups.value = _suggestedGroups.value.toSet()
    }

    private suspend fun loadSuggestion() {
        val sevenDaysAgo = LocalDate.now().minusDays(7).toEpochDay()
        val recentSessions = workoutRepo.getSessionsSince(sevenDaysAgo)

        val lastTrained = recentSessions
            .maxByOrNull { it.startedAtMs }
            ?.muscleGroupIds
            ?.split(",")
            ?.firstOrNull { it.isNotBlank() }

        val suggestion = when (lastTrained) {
            "push"           -> listOf(MuscleGroup.PULL, MuscleGroup.CORE_SHOULDERS)
            "pull"           -> listOf(MuscleGroup.LEGS, MuscleGroup.CORE_SHOULDERS)
            "legs"           -> listOf(MuscleGroup.PUSH, MuscleGroup.CORE_SHOULDERS)
            "core_shoulders" -> listOf(MuscleGroup.PUSH, MuscleGroup.LEGS)
            else             -> listOf(MuscleGroup.PUSH, MuscleGroup.CORE_SHOULDERS)
        }
        _suggestedGroups.value = suggestion
    }
}
