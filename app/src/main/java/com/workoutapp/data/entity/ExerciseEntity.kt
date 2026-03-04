package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val muscleGroupId: String,
    val equipment: String,
    val defaultSets: Int,
    val defaultReps: Int,
    val defaultWeightKg: Float,
    val tempoEccentric: Int,
    val tempoBottomPause: Int,
    val tempoConcentric: Int,
    val tempoTopPause: Int,
    val hasTempoGuidance: Boolean,
    val isShoulderSafe: Boolean,
    val restSeconds: Int,
    val notes: String,
    val sortOrder: Int = 0
)
