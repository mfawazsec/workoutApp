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

        ExerciseEntity(
            id = "push_incline_db_press_bed",
            name = "Incline DB Press (Bed)",
            muscleGroupId = "push",
            equipment = "Dumbbell, Bed",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Upper back on bed edge at 30-45 deg. Do NOT lower DBs past torso plane. Upper chest focus.",
            sortOrder = 7
        ),
        ExerciseEntity(
            id = "push_decline_pushup_bed",
            name = "Decline Push-Up (Feet on Bed)",
            muscleGroupId = "push",
            equipment = "Bodyweight, Bed",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 0f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Feet elevated on bed. Upper chest emphasis. Elbows at 45 deg to body — never flare.",
            sortOrder = 8
        ),
        ExerciseEntity(
            id = "push_tricep_overhead_ext",
            name = "Overhead Tricep Extension",
            muscleGroupId = "push",
            equipment = "Dumbbell",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 8f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Seated or lying. Arms alongside ears, elbows pointing forward. Maximises long-head tricep stretch.",
            sortOrder = 9
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

        ExerciseEntity(
            id = "pull_single_arm_row_bed",
            name = "Single-Arm Row (Bed Support)",
            muscleGroupId = "pull",
            equipment = "Dumbbell, Bed",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 12f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Hand and knee braced on bed edge. Heavier load, deeper stretch at the bottom.",
            sortOrder = 7
        ),
        ExerciseEntity(
            id = "pull_prone_y_raise",
            name = "Prone Y-Raise",
            muscleGroupId = "pull",
            equipment = "Dumbbell, Mat",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 2f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 2,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Lie prone. Arms form a Y overhead at 45 deg. Very light weight. Lower trap + rear delt rehab.",
            sortOrder = 8
        ),
        ExerciseEntity(
            id = "pull_prone_w_raise",
            name = "Prone W-Raise",
            muscleGroupId = "pull",
            equipment = "Dumbbell, Mat",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 2f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 2,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Lie prone. Elbows at 90 deg in W-shape. Externally rotate upward. Critical anterior shoulder rehab.",
            sortOrder = 9
        ),
        ExerciseEntity(
            id = "pull_incline_curl_bed",
            name = "Incline Curl (Bed Reclined)",
            muscleGroupId = "pull",
            equipment = "Dumbbell, Bed",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 8f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Reclined at 45 deg. Shoulder extended = long-head bicep stretch. Best length-tension position.",
            sortOrder = 10
        ),
        ExerciseEntity(
            id = "pull_concentration_curl",
            name = "Concentration Curl",
            muscleGroupId = "pull",
            equipment = "Dumbbell",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 8f,
            tempoEccentric = 3, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Seated, elbow braced on inner thigh. Fully isolates bicep. Supinate at top for full contraction.",
            sortOrder = 11
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

        ExerciseEntity(
            id = "legs_bulgarian_split_squat_bed",
            name = "Bulgarian Split Squat (Bed)",
            muscleGroupId = "legs",
            equipment = "Dumbbell, Bed",
            defaultSets = 4, defaultReps = 10, defaultWeightKg = 8f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Rear foot elevated on bed. Deep quad stretch. Upright torso = quad; forward lean = glute.",
            sortOrder = 7
        ),
        ExerciseEntity(
            id = "legs_wall_sit",
            name = "Wall Sit",
            muscleGroupId = "legs",
            equipment = "Bodyweight",
            defaultSets = 3, defaultReps = 1, defaultWeightKg = 0f,
            tempoEccentric = 0, tempoBottomPause = 0, tempoConcentric = 0, tempoTopPause = 0,
            hasTempoGuidance = false, isShoulderSafe = true, restSeconds = 60,
            notes = "Back flat to wall, thighs parallel to floor. Hold 30-60s. High TUT, zero shoulder demand.",
            sortOrder = 8
        ),
        ExerciseEntity(
            id = "legs_hamstring_curl_bed",
            name = "Prone Hamstring Curl (Bed-Anchored)",
            muscleGroupId = "legs",
            equipment = "Bodyweight, Bed",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 0f,
            tempoEccentric = 3, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Lie prone, hook heels under bed base overhang. Curl heels toward glutes. Pair with RDLs.",
            sortOrder = 9
        ),
        ExerciseEntity(
            id = "legs_single_leg_rdl",
            name = "Single-Leg RDL",
            muscleGroupId = "legs",
            equipment = "Dumbbell",
            defaultSets = 3, defaultReps = 10, defaultWeightKg = 8f,
            tempoEccentric = 3, tempoBottomPause = 1, tempoConcentric = 2, tempoTopPause = 0,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 60,
            notes = "Hinge on one leg, free leg swings back as counterbalance. Light load — control over weight.",
            sortOrder = 10
        ),
        ExerciseEntity(
            id = "legs_hip_thrust_bed",
            name = "Hip Thrust (Bed-Elevated)",
            muscleGroupId = "legs",
            equipment = "Dumbbell, Bed",
            defaultSets = 4, defaultReps = 12, defaultWeightKg = 14f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 1, tempoTopPause = 2,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 90,
            notes = "Upper back on bed edge at shoulder-blade height. DB on hips. Drive to ceiling, 2s squeeze at top.",
            sortOrder = 11
        ),
        ExerciseEntity(
            id = "legs_calf_raise_single",
            name = "Single-Leg Calf Raise",
            muscleGroupId = "legs",
            equipment = "Dumbbell",
            defaultSets = 4, defaultReps = 15, defaultWeightKg = 10f,
            tempoEccentric = 3, tempoBottomPause = 2, tempoConcentric = 1, tempoTopPause = 2,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Stand on one foot. DB in same-side hand. Full stretch at bottom (2s), full rise at top (2s squeeze).",
            sortOrder = 12
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
        ),
        ExerciseEntity(
            id = "core_side_plank",
            name = "Side Plank",
            muscleGroupId = "core_shoulders",
            equipment = "Bodyweight, Mat",
            defaultSets = 3, defaultReps = 1, defaultWeightKg = 0f,
            tempoEccentric = 0, tempoBottomPause = 0, tempoConcentric = 0, tempoTopPause = 0,
            hasTempoGuidance = false, isShoulderSafe = true, restSeconds = 45,
            notes = "Lie on forearm. Body in straight line. Hold 20-40s per side. Anti-lateral flexion core work.",
            sortOrder = 7
        ),
        ExerciseEntity(
            id = "core_external_rotation_lying",
            name = "Side-Lying External Rotation",
            muscleGroupId = "core_shoulders",
            equipment = "Dumbbell, Mat",
            defaultSets = 3, defaultReps = 15, defaultWeightKg = 2f,
            tempoEccentric = 2, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 2,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Lie on non-working side. Elbow at 90 deg tucked to ribs. Rotate forearm upward. 1-3 kg only. Critical anterior dislocation rehab.",
            sortOrder = 8
        ),
        ExerciseEntity(
            id = "core_front_raise_neutral",
            name = "Neutral-Grip Front Raise",
            muscleGroupId = "core_shoulders",
            equipment = "Dumbbell",
            defaultSets = 3, defaultReps = 12, defaultWeightKg = 5f,
            tempoEccentric = 3, tempoBottomPause = 0, tempoConcentric = 2, tempoTopPause = 1,
            hasTempoGuidance = true, isShoulderSafe = true, restSeconds = 45,
            notes = "Palms facing each other (neutral grip). Raise to shoulder height only. Pure sagittal-plane — no external rotation stress.",
            sortOrder = 9
        )
    )
}
