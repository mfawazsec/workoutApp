package com.workoutapp.data.model

import com.workoutapp.data.entity.ExerciseEntity

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroupId: String,
    val subGroupId: String,
    val equipment: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Float,
    val tempo: Tempo?,
    val isShoulderSafe: Boolean,
    val restSeconds: Int,
    val notes: String,
    val sortOrder: Int = 0,
    val isTimeBased: Boolean = false
)

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    muscleGroupId = muscleGroupId,
    subGroupId = subGroupId,
    equipment = equipment,
    sets = defaultSets,
    reps = defaultReps,
    weightKg = defaultWeightKg,
    tempo = if (hasTempoGuidance) Tempo(tempoEccentric, tempoBottomPause, tempoConcentric, tempoTopPause) else null,
    isShoulderSafe = isShoulderSafe,
    restSeconds = restSeconds,
    notes = notes,
    sortOrder = sortOrder,
    isTimeBased = isTimeBased
)
