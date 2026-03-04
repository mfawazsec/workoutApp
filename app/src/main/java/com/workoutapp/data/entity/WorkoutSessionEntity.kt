package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey val sessionId: String,
    val dateEpochDay: Long,
    val muscleGroupIds: String,
    val exerciseIds: String,
    val completedAt: Long? = null
)
