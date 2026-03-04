package com.workoutapp.data.model

data class WorkoutSession(
    val sessionId: String,
    val dateEpochDay: Long,
    val muscleGroups: List<MuscleGroup>,
    val exercises: List<Exercise>,
    val completedAt: Long? = null
)
