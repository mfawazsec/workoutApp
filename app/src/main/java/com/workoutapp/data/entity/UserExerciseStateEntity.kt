package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_exercise_state")
data class UserExerciseStateEntity(
    @PrimaryKey val exerciseId: String,
    val currentWeightKg: Float,
    val currentReps: Int,
    val currentSets: Int,
    val estimatedOneRepMaxKg: Float,
    val lastUpdatedEpoch: Long
)
