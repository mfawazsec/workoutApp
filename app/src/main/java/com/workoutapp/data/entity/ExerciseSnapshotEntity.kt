package com.workoutapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One row per exercise per session — written at session start, completedAtMs filled when all sets are done. */
@Entity(tableName = "exercise_snapshots")
data class ExerciseSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val exerciseId: String,
    val exerciseName: String,
    val sets: Int,
    val weightKg: Float,
    val reps: Int,
    val isTimeBased: Boolean = false,
    val completedAtMs: Long? = null,
    /** Comma-separated ms timestamps, one per completed set. Empty if no sets done. */
    val setCompletionTimestamps: String = ""
)
