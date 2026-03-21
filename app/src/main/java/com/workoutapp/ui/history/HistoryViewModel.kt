package com.workoutapp.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import com.workoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SessionWithSnapshots(
    val session: WorkoutSessionEntity,
    val snapshots: List<ExerciseSnapshotEntity>
)

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val db = WorkoutDatabase.getInstance(application)
    private val repo = WorkoutRepository(
        db.workoutSessionDao(),
        db.userExerciseStateDao(),
        db.progressionLogDao(),
        db.exerciseSnapshotDao()
    )

    val completedSessions: StateFlow<List<WorkoutSessionEntity>> = repo
        .getCompletedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sessionDetails = kotlinx.coroutines.flow.MutableStateFlow<Map<String, List<ExerciseSnapshotEntity>>>(emptyMap())
    val sessionDetails: StateFlow<Map<String, List<ExerciseSnapshotEntity>>> = _sessionDetails

    fun loadSnapshots(sessionId: String) {
        if (_sessionDetails.value.containsKey(sessionId)) return
        viewModelScope.launch {
            val snapshots = repo.getSnapshotsForSession(sessionId)
            _sessionDetails.value = _sessionDetails.value + (sessionId to snapshots)
        }
    }
}
