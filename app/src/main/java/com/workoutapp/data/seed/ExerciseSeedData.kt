package com.workoutapp.data.seed

import com.workoutapp.data.entity.ExerciseEntity

object ExerciseSeedData {
    val exercises: List<ExerciseEntity> = listOf(

        // ── PUSH: Chest + Triceps ────────────────────────────────────────────
        ExerciseEntity(
            id = "push_floor_press",
            name = "Floor Press",
            muscleGroupId = "push",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Keep elbows at 45°. Press until arms are straight. Safe for anterior shoulder.",
            sortOrder = 1
        ),
        ExerciseEntity(
            id = "push_pushup",
            name = "Push-Up",
            muscleGroupId = "push",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 0f,
            tempoEccentric = 2, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Full range — chest to floor. Elevate feet for progression.",
            sortOrder = 2
        ),
        ExerciseEntity(
            id = "push_chest_fly",
            name = "Chest Fly (Floor)",
            muscleGroupId = "push",
            equipment = "5 kg DB",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 5f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Slight elbow bend throughout. Stop when elbows reach the floor.",
            sortOrder = 3
        ),
        ExerciseEntity(
            id = "push_tricep_kickback",
            name = "Tricep Kickback",
            muscleGroupId = "push",
            equipment = "5 kg DB",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 5f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Hinge at hips, upper arm parallel to floor. Extend fully at top.",
            sortOrder = 4
        ),
        ExerciseEntity(
            id = "push_diamond_pushup",
            name = "Diamond Push-Up",
            muscleGroupId = "push",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 0f,
            tempoEccentric = 2, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Hands close together forming a diamond. Emphasises triceps.",
            sortOrder = 5
        ),
        ExerciseEntity(
            id = "push_incline_pushup",
            name = "Incline Push-Up",
            muscleGroupId = "push",
            equipment = "Bodyweight / Chair",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 0f,
            tempoEccentric = 2, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Hands on elevated surface. Good warm-up or finisher variation.",
            sortOrder = 6
        ),

        // ── PULL: Back + Biceps ─────────────────────────────────────────────
        // Horizontal pulls ONLY — no overhead pulling (shoulder safety)
        ExerciseEntity(
            id = "pull_bent_over_row",
            name = "Bent-Over Row",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Hinge to ~45°. Row to lower ribs. Elbow tracks back, not up.",
            sortOrder = 1
        ),
        ExerciseEntity(
            id = "pull_single_arm_row",
            name = "Single-Arm Row",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Brace on knee or bench. Drive the elbow back, not up.",
            sortOrder = 2
        ),
        ExerciseEntity(
            id = "pull_renegade_row",
            name = "Renegade Row",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 3, defaultReps = 8, defaultWeightKg = 10f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Plank position. Anti-rotation core engagement. Keep hips level.",
            sortOrder = 3
        ),
        ExerciseEntity(
            id = "pull_bicep_curl",
            name = "Bicep Curl",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Full range: full extension to full flexion. No swinging.",
            sortOrder = 4
        ),
        ExerciseEntity(
            id = "pull_hammer_curl",
            name = "Hammer Curl",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Neutral grip. Trains brachialis and brachioradialis.",
            sortOrder = 5
        ),
        ExerciseEntity(
            id = "pull_reverse_grip_row",
            name = "Reverse Grip Row",
            muscleGroupId = "pull",
            equipment = "10 kg DB",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Underhand grip on the row. Greater bicep involvement.",
            sortOrder = 6
        ),

        // ── LEGS: Quads / Hamstrings / Glutes ───────────────────────────────
        ExerciseEntity(
            id = "legs_goblet_squat",
            name = "Goblet Squat",
            muscleGroupId = "legs",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 12, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Hold DB at chest. Knees track over toes. Deep range of motion.",
            sortOrder = 1
        ),
        ExerciseEntity(
            id = "legs_rdl",
            name = "Dumbbell RDL",
            muscleGroupId = "legs",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Hinge at hip. Soft knee bend. Feel hamstring stretch at bottom.",
            sortOrder = 2
        ),
        ExerciseEntity(
            id = "legs_reverse_lunge",
            name = "Reverse Lunge",
            muscleGroupId = "legs",
            equipment = "5 kg DB",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 5f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Step back, lower rear knee to 1 inch off floor. More knee-friendly than forward lunge.",
            sortOrder = 3
        ),
        ExerciseEntity(
            id = "legs_sumo_squat",
            name = "Sumo Squat",
            muscleGroupId = "legs",
            equipment = "10 kg DB",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Wide stance, toes out 45°. Hold DB between legs. Inner thigh emphasis.",
            sortOrder = 4
        ),
        ExerciseEntity(
            id = "legs_glute_bridge",
            name = "Glute Bridge",
            muscleGroupId = "legs",
            equipment = "10 kg DB",
            defaultSets = 4, defaultReps = 15, defaultWeightKg = 10f,
            tempoEccentric = 2, tempoBottomPause = 2, tempoConcentric = 1, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "DB on hips. Drive hips to ceiling, pause at top. Squeeze glutes.",
            sortOrder = 5
        ),
        ExerciseEntity(
            id = "legs_step_up",
            name = "Step-Up",
            muscleGroupId = "legs",
            equipment = "5 kg DB / Bodyweight",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 5f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Use a sturdy chair or step. Drive through the heel of the working leg.",
            sortOrder = 6
        ),

        // ── CORE + SHOULDERS (Rehab-Safe) ───────────────────────────────────
        // No overhead press. No upright rows. Rehab-focused shoulder work only.
        ExerciseEntity(
            id = "core_plank",
            name = "Plank",
            muscleGroupId = "core_shoulders",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 1, defaultWeightKg = 0f,
            tempoEccentric = 0, tempoBottomPause = 0, tempoConcentric = 0, tempoTopPause = 0,
            hasTempoGuidance = false, isShoulderSafe = true, restSeconds = 60,
            notes = "Forearms or hands. Neutral spine. Brace as if taking a punch. Hold 30–60s.",
            sortOrder = 1
        ),
        ExerciseEntity(
            id = "core_dead_bug",
            name = "Dead Bug",
            muscleGroupId = "core_shoulders",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 0f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Lower back pressed to floor throughout. Opposite arm + leg extend slowly.",
            sortOrder = 2
        ),
        ExerciseEntity(
            id = "core_bird_dog",
            name = "Bird Dog",
            muscleGroupId = "core_shoulders",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 0f,
            tempoEccentric = 2, tempoBottomPause = 2, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "On all fours. Extend opposite arm + leg. Hips level — no rotation.",
            sortOrder = 3
        ),
        ExerciseEntity(
            id = "core_lateral_raise",
            name = "Lateral Raise",
            muscleGroupId = "core_shoulders",
            equipment = "5 kg DB",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 5f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "LIGHT weight only (5 kg max). Raise to 90° (shoulder height). No higher — keeps anterior shoulder safe.",
            sortOrder = 4
        ),
        ExerciseEntity(
            id = "core_external_rotation",
            name = "External Rotation",
            muscleGroupId = "core_shoulders",
            equipment = "5 kg DB",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 5f,
            tempoEccentric = 2, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Elbow at 90°, tucked to side. Rotate forearm outward. Key rehab exercise for anterior shoulder.",
            sortOrder = 5
        ),
        ExerciseEntity(
            id = "core_superman",
            name = "Superman",
            muscleGroupId = "core_shoulders",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 0f,
            tempoEccentric = 1, tempoBottomPause = 3, tempoConcentric = 1, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Prone on floor. Lift chest and legs simultaneously. Posterior chain + scapular retraction.",
            sortOrder = 6
        )
    )
}
