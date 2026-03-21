package com.workoutapp.data.model

data class WarmupCooldownItem(
    val id: String,
    val name: String,
    val durationSeconds: Int? = null,
    val reps: Int? = null,
    val instructions: String,
    val type: Type
) {
    enum class Type { WARMUP, COOLDOWN }

    val displayDuration: String get() = when {
        durationSeconds != null -> "${durationSeconds}s"
        reps != null -> "${reps} reps"
        else -> ""
    }
}
