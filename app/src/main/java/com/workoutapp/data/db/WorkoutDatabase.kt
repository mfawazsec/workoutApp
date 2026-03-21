package com.workoutapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.workoutapp.data.entity.DailyTrackingEntity
import com.workoutapp.data.entity.ExerciseEntity
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import com.workoutapp.data.entity.ProgressionLogEntity
import com.workoutapp.data.entity.UserExerciseStateEntity
import com.workoutapp.data.entity.WorkoutSessionEntity

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        UserExerciseStateEntity::class,
        ProgressionLogEntity::class,
        DailyTrackingEntity::class,
        ExerciseSnapshotEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun userExerciseStateDao(): UserExerciseStateDao
    abstract fun progressionLogDao(): ProgressionLogDao
    abstract fun dailyTrackingDao(): DailyTrackingDao
    abstract fun exerciseSnapshotDao(): ExerciseSnapshotDao

    companion object {
        @Volatile private var INSTANCE: WorkoutDatabase? = null

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE exercises ADD COLUMN isTimeBased INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE exercise_snapshots ADD COLUMN isTimeBased INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE exercises SET isTimeBased = 1, defaultReps = 30 WHERE id IN ('legs_wall_sit', 'core_plank')")
                db.execSQL("UPDATE user_exercise_state SET currentReps = 30 WHERE exerciseId IN ('legs_wall_sit', 'core_plank') AND currentReps = 1")
                db.execSQL("UPDATE exercises SET isShoulderSafe = 0 WHERE id = 'push_chest_fly'")
                db.execSQL("DELETE FROM exercises WHERE id = 'core_side_plank'")
                db.execSQL("DELETE FROM user_exercise_state WHERE exerciseId = 'core_side_plank'")
                db.execSQL("""INSERT INTO exercises (id, name, muscleGroupId, subGroupId, equipment, defaultSets, defaultReps, defaultWeightKg, tempoEccentric, tempoBottomPause, tempoConcentric, tempoTopPause, hasTempoGuidance, isShoulderSafe, restSeconds, notes, sortOrder, isTimeBased) VALUES ('core_suitcase_carry', 'Suitcase Carry', 'core_shoulders', 'core_stability', 'Dumbbell', 3, 30, 10.0, 0, 0, 0, 0, 0, 1, 45, 'Hold a single DB at your side, stand tall. Walk in place or pace the room — do NOT lean sideways. Resist the lateral pull the entire hold. 30s per side.', 9, 1)""")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add per-set timestamp tracking
                db.execSQL("ALTER TABLE exercise_snapshots ADD COLUMN setCompletionTimestamps TEXT NOT NULL DEFAULT ''")

                // Lower all dumbbell defaults to 5 kg (injury recovery)
                db.execSQL("UPDATE exercises SET defaultWeightKg = 5.0 WHERE defaultWeightKg >= 8.0")
                db.execSQL("UPDATE exercises SET equipment = REPLACE(equipment, '10 kg DB', '5 kg DB') WHERE equipment LIKE '%10 kg DB%'")

                // Insert new exercises added in v5 (using INSERT OR IGNORE to be safe)
                val ins = "INSERT OR IGNORE INTO exercises (id, name, muscleGroupId, subGroupId, equipment, defaultSets, defaultReps, defaultWeightKg, tempoEccentric, tempoBottomPause, tempoConcentric, tempoTopPause, hasTempoGuidance, isShoulderSafe, restSeconds, notes, sortOrder, isTimeBased) VALUES"

                // push_chest
                db.execSQL("$ins ('push_close_grip_pushup','Close-Grip Push-Up','push','push_chest','Bodyweight',3,12,0.0,2,1,2,0,1,1,60,'Hands shoulder-width or narrower. Hits inner chest and triceps simultaneously. Elbows track alongside torso.',7,0)")
                db.execSQL("$ins ('push_archer_pushup','Archer Push-Up','push','push_chest','Bodyweight',3,8,0.0,3,1,2,0,1,1,75,'Wide stance. Lower to one side while extending opposite arm straight. Deep unilateral chest stretch. Count each side.',8,0)")
                db.execSQL("$ins ('push_wide_pushup','Wide Push-Up','push','push_chest','Bodyweight',3,15,0.0,2,1,2,0,1,1,45,'Hands wider than shoulder-width. Emphasises outer and lower pec. Keep core tight.',9,0)")
                db.execSQL("$ins ('push_db_squeeze_press','DB Squeeze Press','push','push_chest','5 kg DB',3,12,5.0,3,1,2,1,1,1,60,'Press DBs together and maintain inward force throughout. Constant pec tension.',10,0)")
                db.execSQL("$ins ('push_db_pullover','DB Pullover','push','push_chest','5 kg DB',3,12,5.0,3,1,2,0,1,1,60,'Lie on bed, lower DB behind head in arc. Feel chest and serratus stretch. Do NOT overextend at bottom.',11,0)")
                db.execSQL("$ins ('push_staggered_pushup','Staggered Push-Up','push','push_chest','Bodyweight',3,10,0.0,2,1,2,0,1,1,60,'One hand forward, one back. Shifts load to leading-side pec. Alternate hands between sets.',12,0)")
                db.execSQL("$ins ('push_typewriter_pushup','Typewriter Push-Up','push','push_chest','Bodyweight',3,8,0.0,2,0,2,0,1,1,75,'Wide hands. Lower to one side, slide across, press up on other. Lateral chest emphasis.',13,0)")
                db.execSQL("$ins ('push_pike_pushup','Pike Push-Up','push','push_chest','Bodyweight',3,10,0.0,2,1,2,0,1,1,60,'Hips high in inverted V. Lower head toward floor between hands. Upper chest and front delt.',14,0)")

                // push_triceps
                db.execSQL("$ins ('push_chair_dip','Chair Dip','push','push_triceps','Bodyweight, Chair',3,12,0.0,3,1,2,0,1,1,60,'Hands on chair edge behind you. Lower until elbows reach 90. Upright torso = more tricep.',18,0)")
                db.execSQL("$ins ('push_skull_crusher','Skull Crusher (DB)','push','push_triceps','5 kg DB',3,12,5.0,3,1,2,0,1,1,60,'Lying flat. DBs start over forehead, lower toward temples. Long-head dominant. Keep upper arms vertical.',19,0)")
                db.execSQL("$ins ('push_two_arm_kickback','Two-Arm Kickback','push','push_triceps','5 kg DB',3,15,5.0,2,0,2,2,1,1,45,'Both arms simultaneously. 2s squeeze at lockout every rep. High-volume lateral head finisher.',20,0)")
                db.execSQL("$ins ('push_tate_press','Tate Press','push','push_triceps','5 kg DB',3,12,5.0,2,1,2,0,1,1,60,'Lying flat, DBs start over chest, elbows flared outward. Lower DBs to chest bending elbows out. Inner tricep.',21,0)")

                // pull_back
                db.execSQL("$ins ('pull_meadows_row','Meadows Row','pull','pull_back','5 kg DB',4,10,5.0,3,1,2,0,1,1,75,'Stand perpendicular, DB on floor. Row with elbow-out flare to hit upper-back width.',8,0)")
                db.execSQL("$ins ('pull_chest_supported_row','Chest-Supported Row (Bed)','pull','pull_back','5 kg DB, Bed',4,12,5.0,3,1,2,1,1,1,75,'Lie prone on bed edge. Row both DBs to lower ribs. Pure lat and mid-back stimulus.',9,0)")
                db.execSQL("$ins ('pull_inverted_row','Inverted Row (Under Table)','pull','pull_back','Bodyweight, Table',3,10,0.0,3,1,2,0,1,1,60,'Lie under a sturdy table. Row chest to underside. Extend legs for harder variation.',10,0)")
                db.execSQL("$ins ('pull_batwing_row','Batwing Row','pull','pull_back','5 kg DB, Bed',3,10,5.0,2,3,2,0,1,1,60,'Prone on bed. Row both DBs and HOLD at top for 3s each rep. Builds mid-back density.',11,0)")
                db.execSQL("$ins ('pull_kroc_row','Kroc Row','pull','pull_back','5 kg DB, Bed',3,15,5.0,1,0,1,1,1,1,90,'Heavy single-arm row, high reps, controlled momentum. Grip and pulling endurance.',12,0)")
                db.execSQL("$ins ('pull_prone_trap_raise','Prone Trap Raise','pull','pull_back','Dumbbell, Mat',3,12,2.0,2,0,2,2,1,1,45,'Lie prone, arms by sides, thumbs up. Raise straight arms using lower traps. 2 kg only.',13,0)")
                db.execSQL("$ins ('pull_db_pullover_lat','DB Pullover (Lat Focus)','pull','pull_back','5 kg DB',3,12,5.0,3,1,2,0,1,1,60,'Lie on bed, bent elbows. Focus on pulling with lats not chest. V-taper builder.',14,0)")

                // pull_biceps
                db.execSQL("$ins ('pull_crossbody_curl','Cross-Body Hammer Curl','pull','pull_biceps','5 kg DB',3,12,5.0,3,0,2,1,1,1,45,'Curl across body toward opposite shoulder. Brachialis emphasis. Alternate arms.',19,0)")
                db.execSQL("$ins ('pull_reverse_curl','Reverse Curl','pull','pull_biceps','5 kg DB',3,12,5.0,3,0,2,1,1,1,45,'Overhand pronated grip. Targets brachialis and brachioradialis. Builds forearm thickness.',20,0)")
                db.execSQL("$ins ('pull_zottman_curl','Zottman Curl','pull','pull_biceps','5 kg DB',3,10,5.0,3,0,2,0,1,1,60,'Curl up supinated, rotate to pronated at top, lower overhand. Trains all three elbow flexors.',21,0)")
                db.execSQL("$ins ('pull_drag_curl','Drag Curl','pull','pull_biceps','5 kg DB',3,12,5.0,3,0,2,1,1,1,45,'Keep DBs dragging up the torso as you curl. Eliminates front delt help. Pure bicep isolation.',22,0)")

                // legs_quads
                db.execSQL("$ins ('legs_heel_elevated_goblet','Heel-Elevated Goblet Squat','legs','legs_quads','5 kg DB',4,12,5.0,3,1,2,0,1,1,75,'Heels on folded towel. Increases knee travel and VMO engagement. Builds the teardrop quad.',7,0)")
                db.execSQL("$ins ('legs_lateral_lunge','Lateral Lunge','legs','legs_quads','5 kg DB',3,10,5.0,2,1,2,0,1,1,60,'Step wide to one side, sit hip back. Adductors and glutes. Count per side.',8,0)")
                db.execSQL("$ins ('legs_curtsy_lunge','Curtsy Lunge','legs','legs_quads','5 kg DB',3,10,5.0,2,0,2,0,1,1,60,'Step diagonally behind and across. Hits glute medius and outer sweep. Count per side.',9,0)")
                db.execSQL("$ins ('legs_sissy_squat','Sissy Squat','legs','legs_quads','Bodyweight',3,10,0.0,3,1,2,0,1,1,75,'Hold for balance. Lean back as knees travel forward. Maximum VMO stretch.',10,0)")
                db.execSQL("$ins ('legs_pistol_assist','Assisted Pistol Squat','legs','legs_quads','Bodyweight, Chair',3,6,0.0,3,1,2,0,1,1,90,'Hold chair lightly for balance. One leg extended, squat deep on working leg. Count per side.',11,0)")
                db.execSQL("$ins ('legs_pulse_squat','Pulse Squat','legs','legs_quads','Bodyweight',3,20,0.0,0,0,0,0,0,1,45,'Hold squat at parallel. Small continuous pulses. Accumulates TUT without joint stress.',12,0)")

                // legs_hamstrings
                db.execSQL("$ins ('legs_good_morning','Good Morning (DB)','legs','legs_hamstrings','5 kg DB',3,12,5.0,3,1,2,0,1,1,75,'DB behind head. Hip hinge with slight knee bend. Hamstrings and spinal erectors.',16,0)")
                db.execSQL("$ins ('legs_stiff_leg_dl','Stiff-Leg Deadlift (DB)','legs','legs_hamstrings','5 kg DB',4,10,5.0,3,1,2,0,1,1,90,'Knees nearly locked. Hinge deep to mid-shin. Maximum hamstring stretch.',17,0)")
                db.execSQL("$ins ('legs_nordic_curl','Nordic Hamstring Curl','legs','legs_hamstrings','Bodyweight, Sofa',3,6,0.0,4,0,2,0,1,1,90,'Anchor heels under sofa. Lower torso as slowly as possible. Use hands to push back. Best hamstring exercise.',18,0)")
                db.execSQL("$ins ('legs_lying_curl_towel','Lying Hamstring Curl (Towel)','legs','legs_hamstrings','Bodyweight, Towel',3,12,0.0,3,0,2,1,1,1,60,'Lie on floor with towel under heels. Bridge hips, slide heels in to curl, slide back.',19,0)")
                db.execSQL("$ins ('legs_deadlift_conventional','DB Conventional Deadlift','legs','legs_hamstrings','5 kg DB',4,8,5.0,3,1,2,0,1,1,90,'DBs beside feet. Hinge down, keep back flat, drive through floor. Full posterior chain.',20,0)")

                // legs_glutes
                db.execSQL("$ins ('legs_single_glute_bridge','Single-Leg Glute Bridge','legs','legs_glutes','Bodyweight',3,15,0.0,2,2,1,0,1,1,45,'One foot on floor. Drives unilateral glute activation. Count per side.',23,0)")
                db.execSQL("$ins ('legs_frog_pump','Frog Pump','legs','legs_glutes','Bodyweight',3,20,0.0,1,0,1,2,1,1,45,'Lie on back, soles together, knees out. Bridge hips up squeezing glutes. Zero lower back load.',24,0)")
                db.execSQL("$ins ('legs_sumo_rdl','Sumo RDL','legs','legs_glutes','5 kg DB',4,12,5.0,3,1,2,0,1,1,75,'Wide stance, toes out. DB between legs. Glutes and adductors emphasis.',25,0)")
                db.execSQL("$ins ('legs_db_kickback_kneeling','Kneeling DB Kickback','legs','legs_glutes','5 kg DB',3,15,5.0,2,0,2,2,1,1,45,'On all fours, DB in crook of knee. Drive heel to ceiling. 2s hold. Count per side.',26,0)")

                // legs_calves
                db.execSQL("$ins ('legs_calf_raise_double','Standing Calf Raise','legs','legs_calves','5 kg DB',4,20,5.0,3,2,1,2,1,1,45,'Both feet. Full range — full plantar flexion at top, full stretch at bottom.',28,0)")
                db.execSQL("$ins ('legs_seated_calf_raise','Seated Calf Raise','legs','legs_calves','5 kg DB',4,15,5.0,3,2,1,2,1,1,45,'Seated, DB on knee. Targets soleus for calf thickness from all angles.',29,0)")
                db.execSQL("$ins ('legs_explosive_calf','Explosive Calf Jump','legs','legs_calves','Bodyweight',3,15,0.0,0,0,0,0,0,1,45,'Jump through ankle extension. Land softly. Elastic calf strength and Achilles tendon.',30,0)")

                // core_shoulders (shoulder subgroup)
                db.execSQL("$ins ('core_rear_delt_fly','Rear Delt Fly (Prone)','core_shoulders','core_shoulders','Dumbbell, Mat',3,15,2.0,2,0,2,2,1,1,45,'Lie prone. Raise arms out to sides, pinch shoulder blades. 2s hold. Builds 3D rear delt.',5,0)")
                db.execSQL("$ins ('core_face_pull_band','Face Pull (Band)','core_shoulders','core_shoulders','Resistance Band',3,15,0.0,2,1,2,0,1,1,45,'Anchor band low. Pull toward face splitting hands apart, externally rotating at top. Best shoulder health exercise.',6,0)")
                db.execSQL("$ins ('core_db_overhead_press','DB Overhead Press (Seated)','core_shoulders','core_shoulders','5 kg DB',3,12,5.0,3,1,2,0,1,1,90,'Seated, neutral grip. Press directly overhead. Full overhead extension builds anterior and medial delt.',7,0)")
                db.execSQL("$ins ('core_reverse_fly_bent','Reverse Fly (Bent-Over)','core_shoulders','core_shoulders','Dumbbell',3,15,2.0,2,0,2,2,1,1,45,'Hinge at hips, raise arms out to sides. Rear delt and mid-trap. 2s hold at top. Light weight.',8,0)")

                // core_stability
                db.execSQL("$ins ('core_hollow_body','Hollow Body Hold','core_shoulders','core_stability','Bodyweight',3,30,0.0,0,0,0,0,0,1,60,'Back flat, legs 15cm off floor, arms overhead. Gymnastics core foundation.',14,1)")
                db.execSQL("$ins ('core_v_up','V-Up','core_shoulders','core_stability','Bodyweight',3,12,0.0,2,0,2,1,1,1,60,'Raise straight legs and torso to meet in middle. Full rectus abdominis range.',15,0)")
                db.execSQL("$ins ('core_russian_twist','Russian Twist (DB)','core_shoulders','core_stability','5 kg DB',3,20,5.0,1,0,1,1,1,1,45,'Seated, lean 45 back. Rotate side to side, 1s pause each end. Oblique dominant.',16,0)")
                db.execSQL("$ins ('core_side_plank','Side Plank','core_shoulders','core_stability','Bodyweight',3,30,0.0,0,0,0,0,0,1,45,'On forearm and foot. Keep hips up. Obliques and glute medius. Count per side.',17,1)")
                db.execSQL("$ins ('core_mountain_climber','Mountain Climber','core_shoulders','core_stability','Bodyweight',3,30,0.0,0,0,0,0,0,1,45,'High plank. Drive knees to chest alternately. Core, hip flexors, shoulder stability.',18,1)")
                db.execSQL("$ins ('core_l_sit_tuck','L-Sit Tuck (Floor)','core_shoulders','core_stability','Bodyweight',3,20,0.0,0,0,0,0,0,1,60,'Hands on floor, press down to lift hips, tuck knees. Extreme hip flexor and core demand.',19,1)")
                db.execSQL("$ins ('core_ab_rollout','Ab Rollout (DB Substitute)','core_shoulders','core_stability','5 kg DB',3,8,5.0,3,1,2,0,1,1,75,'Kneel, DB sideways on floor, roll forward controlling descent. Pull back with abs. Advanced.',20,0)")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_tracking ADD COLUMN multivitaminTaken INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_db"
                )
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .build().also { INSTANCE = it }
            }
        }
    }
}
