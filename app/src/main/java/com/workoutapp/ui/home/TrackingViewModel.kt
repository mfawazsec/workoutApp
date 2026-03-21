package com.workoutapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutapp.data.db.WorkoutDatabase
import com.workoutapp.data.entity.DailyTrackingEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TrackingViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = WorkoutDatabase.getInstance(application).dailyTrackingDao()
    private val today get() = LocalDate.now().toString()

    // Water: base goal = 8 glasses (250ml each = 2000ml for 59kg/178cm)
    // With creatine: +2 glasses = 10 total
    val BASE_WATER_GOAL = 8
    val CREATINE_WATER_BONUS = 2

    val todayTracking: StateFlow<DailyTrackingEntity> = dao.getByDate(today)
        .map { it ?: DailyTrackingEntity(date = today) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailyTrackingEntity(date = today))

    val waterGoal: StateFlow<Int> = todayTracking
        .map { if (it.creatineServings > 0) BASE_WATER_GOAL + CREATINE_WATER_BONUS else BASE_WATER_GOAL }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BASE_WATER_GOAL)

    fun addWater(delta: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            val newVal = (current.waterGlasses + delta).coerceAtLeast(0)
            dao.upsert(current.copy(waterGlasses = newVal))
        }
    }

    fun setWater(glasses: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            dao.upsert(current.copy(waterGlasses = glasses.coerceAtLeast(0)))
        }
    }

    fun addCreatine(delta: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            val newVal = (current.creatineServings + delta).coerceAtLeast(0)
            dao.upsert(current.copy(creatineServings = newVal))
        }
    }

    fun setCreatine(servings: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            dao.upsert(current.copy(creatineServings = servings.coerceAtLeast(0)))
        }
    }

    fun addMultivitamin(delta: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            dao.upsert(current.copy(multivitaminTaken = (current.multivitaminTaken + delta).coerceIn(0, 1)))
        }
    }

    fun setMultivitamin(value: Int) {
        viewModelScope.launch {
            val current = todayTracking.value
            dao.upsert(current.copy(multivitaminTaken = value.coerceIn(0, 1)))
        }
    }
}
