package com.workoutapp.data.model

import androidx.compose.ui.graphics.Color

enum class MuscleGroup(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val colorHex: Long,
    val bodyRegions: List<BodyRegion>
) {
    PUSH(
        id = "push",
        displayName = "Push",
        subtitle = "Chest · Triceps",
        colorHex = 0xFFFF6B47,
        bodyRegions = listOf(BodyRegion.CHEST, BodyRegion.TRICEPS)
    ),
    PULL(
        id = "pull",
        displayName = "Pull",
        subtitle = "Back · Biceps",
        colorHex = 0xFF4A90D9,
        bodyRegions = listOf(BodyRegion.UPPER_BACK, BodyRegion.BICEPS)
    ),
    LEGS(
        id = "legs",
        displayName = "Legs",
        subtitle = "Quads · Hamstrings · Glutes",
        colorHex = 0xFF51C878,
        bodyRegions = listOf(BodyRegion.QUADS, BodyRegion.HAMSTRINGS, BodyRegion.GLUTES)
    ),
    CORE_SHOULDERS(
        id = "core_shoulders",
        displayName = "Core & Shoulders",
        subtitle = "Core · Rehab Shoulder",
        colorHex = 0xFFB47FD8,
        bodyRegions = listOf(BodyRegion.ABS, BodyRegion.SHOULDERS, BodyRegion.LOWER_BACK)
    );

    val color: Color get() = Color(colorHex)

    companion object {
        fun fromId(id: String): MuscleGroup? = entries.find { it.id == id }
        fun fromIds(ids: List<String>): List<MuscleGroup> = ids.mapNotNull { fromId(it) }
    }
}

enum class BodyRegion {
    CHEST, TRICEPS,
    UPPER_BACK, BICEPS,
    QUADS, HAMSTRINGS, GLUTES,
    ABS, SHOULDERS, LOWER_BACK
}
