package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progression_log")
data class ProgressionLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Int = 0,
    val sessionId: String,
    val exerciseId: String,
    val performedWeightKg: Float,
    val performedReps: Int,
    val performedSets: Int,
    val addedEccentricSeconds: Int,
    val reducedRestSeconds: Int,
    val dateEpochDay: Long,
    val recordedAt: Long
)
