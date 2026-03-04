package com.workoutapp.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.entity.DailyTrackingEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val db = WorkoutDatabase.getInstance(application)

    val recentTracking: StateFlow<List<DailyTrackingEntity>> = db.dailyTrackingDao()
        .getRecentHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _recentSessions = MutableStateFlow<List<WorkoutSessionEntity>>(emptyList())
    val recentSessions: StateFlow<List<WorkoutSessionEntity>> = _recentSessions

    init {
        viewModelScope.launch {
            _recentSessions.value = db.workoutSessionDao().getSessionsSince(0L)
        }
    }
}
