package com.workoutapp.data.util

import kotlin.math.roundToInt

enum class RepZone(val label: String, val colorHex: Long) {
    STRENGTH("Strength zone", 0xFFFFD93D),
    HYPERTROPHY("Hypertrophy zone", 0xFF51C878),
    ENDURANCE("Endurance zone", 0xFFFF9F45),
    OUT_OF_RANGE("Add weight!", 0xFFFF4D4D)
}

object RepMaxCalculator {

    /** Epley formula: 1RM = w × (1 + r/30) */
    fun estimateOneRepMax(weightKg: Float, reps: Int): Float {
        if (reps <= 0 || weightKg <= 0f) return weightKg
        return weightKg * (1f + reps / 30f)
    }

    /** Given a 1RM and a new weight, suggest optimal reps (clamped to 6–20). */
    fun suggestReps(oneRepMaxKg: Float, weightKg: Float): Int {
        if (oneRepMaxKg <= 0f || weightKg <= 0f) return 10
        val suggested = (30f * (oneRepMaxKg / weightKg - 1f)).roundToInt()
        return suggested.coerceIn(6, 20)
    }

    /** Given a 1RM and target reps, suggest the corresponding weight in kg. */
    fun suggestWeight(oneRepMaxKg: Float, targetReps: Int): Float {
        if (oneRepMaxKg <= 0f || targetReps <= 0) return 0f
        return oneRepMaxKg / (1f + targetReps / 30f)
    }

    /** Returns the training zone for a given rep count. */
    fun zone(reps: Int): RepZone = when (reps) {
        in 6..8   -> RepZone.STRENGTH
        in 8..15  -> RepZone.HYPERTROPHY
        in 15..20 -> RepZone.ENDURANCE
        else      -> RepZone.OUT_OF_RANGE
    }
}
