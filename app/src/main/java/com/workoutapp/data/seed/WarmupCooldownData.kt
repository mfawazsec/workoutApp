package com.workoutapp.data.seed

import com.workoutapp.data.model.WarmupCooldownItem
import com.workoutapp.data.model.WarmupCooldownItem.Type.COOLDOWN
import com.workoutapp.data.model.WarmupCooldownItem.Type.WARMUP

object WarmupCooldownData {

    val warmups: Map<String, List<WarmupCooldownItem>> = mapOf(
        "push" to listOf(
            WarmupCooldownItem(
                id = "push_wu_arm_circles",
                name = "Arm Circles",
                durationSeconds = 30,
                instructions = "Stand tall, arms extended out. Make large forward circles (15s) then reverse (15s). Opens the shoulder joint and increases synovial fluid. Start small and widen the arc gradually.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "push_wu_shoulder_rolls",
                name = "Shoulder Rolls",
                reps = 15,
                instructions = "Roll shoulders forward in a large arc 15 times, then backward 15 times. Mobilises the AC joint and warms the rotator cuff before any pressing. Keep the neck relaxed.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "push_wu_wall_slide",
                name = "Wall Slide",
                reps = 10,
                instructions = "Stand with back and arms against a wall in a 'W' shape. Slide arms up to a Y overhead keeping contact. Activates lower traps and serratus anterior — critical scapular stabilisers for safe pressing.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "push_wu_inchworm",
                name = "Inchworm Push-Up",
                reps = 6,
                instructions = "Hinge at hips, walk hands out to a plank. Do one slow push-up, walk hands back in, stand tall. Full-body warm-up that activates chest, triceps, and core while stretching the hamstrings. Slow and controlled.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "push_wu_wrist_circles",
                name = "Wrist Circles",
                reps = 15,
                instructions = "Extend arms forward, make large circles with both wrists clockwise (15) then anticlockwise (15). Essential before any pressing or dip work. Prevents impingement.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "push_wu_prone_y",
                name = "Prone Y-Raise (Activation)",
                reps = 10,
                instructions = "Lie face-down. Arms form a Y overhead at 45°. Raise with thumbs up, hold 1s. Light activation of lower traps and posterior cuff before loading the anterior chain. No weight.",
                type = WARMUP
            )
        ),
        "pull" to listOf(
            WarmupCooldownItem(
                id = "pull_wu_cat_cow",
                name = "Cat-Cow",
                reps = 12,
                instructions = "On all fours, inhale to arch spine (cow), exhale to round it (cat). Mobilises the thoracic spine and prepares the back extensors for heavy rowing. Move slowly through full range each rep.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "pull_wu_scap_retraction",
                name = "Scapular Retractions",
                reps = 15,
                instructions = "Arms extended forward. Pinch shoulder blades together as hard as possible without bending elbows, hold 2s, release fully. Activates the mid-trapezius and rhomboids. Pre-fatigues scapular retractors so they're ready to work during rows.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "pull_wu_chest_opener",
                name = "Doorframe Chest Opener",
                durationSeconds = 30,
                instructions = "Stand in a doorframe, arm bent at 90°, forearm on the frame. Rotate body away until a stretch is felt in the chest and anterior shoulder. Essential counter to tight pecs which limit full lat engagement during rows. 15s per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "pull_wu_thoracic_rotation",
                name = "Thoracic Rotation",
                reps = 10,
                instructions = "Sit or kneel. Hands behind head. Rotate upper body as far as possible to each side. Unlocks the thoracic spine so you can fully retract and depress the scapula during pulling movements. Count per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "pull_wu_band_pullthrough",
                name = "Band Pull-Apart",
                reps = 20,
                instructions = "Hold a band in front at shoulder width. Pull hands apart until arms are fully extended to sides. Activates the rear delts and external rotators before any rowing. If no band, simulate with towel.",
                type = WARMUP
            )
        ),
        "legs" to listOf(
            WarmupCooldownItem(
                id = "legs_wu_hip_circles",
                name = "Hip Circles",
                reps = 15,
                instructions = "Stand on one leg, draw large circles in both directions with the free knee. Opens the hip capsule in all planes of motion. Essential before squatting or hinging. Count 15 each direction per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "legs_wu_leg_swings_fb",
                name = "Leg Swings (Front-Back)",
                reps = 15,
                instructions = "Hold a wall for balance. Swing the free leg forward and back in a controlled pendulum. Lengthens the hip flexors and hamstrings dynamically. Essential before any squat or deadlift pattern. Count per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "legs_wu_leg_swings_lateral",
                name = "Leg Swings (Side-to-Side)",
                reps = 15,
                instructions = "Hold a wall. Swing leg across body and out to side. Opens the inner thigh and glute medius. Particularly important before sumo squats and lateral lunges. Count per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "legs_wu_bw_squat",
                name = "Slow Bodyweight Squat",
                reps = 10,
                instructions = "Deep squat with a 3-second lowering phase. Hands forward for counterbalance. Establishes the movement pattern and warms the knees and hips before adding load. Feel every degree of the range.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "legs_wu_glute_bridge",
                name = "Glute Bridge Activation",
                reps = 15,
                instructions = "Lie on back, drive hips up and hold 2s at the top. Wakes up the glutes before any leg work. Many people find their glutes are inhibited after sitting — this fires them up so they do their job during squats and hinges.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "legs_wu_ankle_circles",
                name = "Ankle Circles",
                reps = 15,
                instructions = "Seated or standing. Draw large circles with each foot clockwise then anticlockwise. Increases dorsiflexion range which directly improves squat depth and heel-elevated exercises.",
                type = WARMUP
            )
        ),
        "core_shoulders" to listOf(
            WarmupCooldownItem(
                id = "cs_wu_neck_semicircles",
                name = "Neck Half-Circles",
                reps = 10,
                instructions = "Slowly drop chin to chest and roll side to side in a semicircle. NEVER roll the head backward. Releases cervical tension. Go very slowly — the neck is delicate. 10 full side-to-side arcs.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "cs_wu_shoulder_cars",
                name = "Shoulder CARs",
                reps = 5,
                instructions = "Controlled Articular Rotations. One arm at a time. Draw the largest possible circle with the shoulder in both directions, actively engaging muscles throughout. Explores full shoulder range under control. Count per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "cs_wu_child_cobra",
                name = "Child's Pose to Cobra",
                reps = 8,
                instructions = "From child's pose (arms extended forward), slide through to cobra (hips down, chest up) by pulling yourself forward. Dynamic flow that opens the thoracic spine and activates the core before stabilisation work.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "cs_wu_thread_needle",
                name = "Thread-the-Needle",
                reps = 8,
                instructions = "On all fours. Thread one arm under the body, rotating thoracic spine until shoulder touches the floor. Return and switch. Unlocks the thoracic spine and rotator cuff simultaneously. Count per side.",
                type = WARMUP
            ),
            WarmupCooldownItem(
                id = "cs_wu_side_twist",
                name = "Standing Side Twist",
                reps = 20,
                instructions = "Feet shoulder-width, arms out to sides. Rotate torso side to side in a relaxed pendulum. Allow arms to swing naturally. Warms up the obliques and prepares the core for stabilisation.",
                type = WARMUP
            )
        )
    )

    val cooldowns: Map<String, List<WarmupCooldownItem>> = mapOf(
        "push" to listOf(
            WarmupCooldownItem(
                id = "push_cd_doorframe_chest",
                name = "Chest Doorframe Stretch",
                durationSeconds = 60,
                instructions = "Stand in a doorframe. Place both forearms on the frame. Gently step through until you feel a deep stretch across the chest and anterior shoulders. Hold 30s. Counteracts all the pressing work. Essential for posture.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "push_cd_crossbody_tricep",
                name = "Cross-Body Tricep Stretch",
                durationSeconds = 60,
                instructions = "Reach one arm across the chest. Use the other hand to gently pull the arm closer. Hold 30s per side. Stretches the long head of the tricep and the posterior deltoid. Never pull hard.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "push_cd_overhead_tricep",
                name = "Overhead Tricep Stretch",
                durationSeconds = 60,
                instructions = "Raise one arm overhead. Bend elbow, dropping the hand behind the head. Gently press the elbow down with the other hand. Hold 30s per side. Stretches the long-head tricep under full length-tension.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "push_cd_pec_floor",
                name = "Pec Minor Stretch (Floor)",
                durationSeconds = 60,
                instructions = "Lie on the floor on one side. Top arm extended at 90° shoulder height, palm down. Gently let the weight of the arm roll the chest open toward the floor. Hold 30s per side. Releases tight pec minor which rounds the shoulder forward.",
                type = COOLDOWN
            )
        ),
        "pull" to listOf(
            WarmupCooldownItem(
                id = "pull_cd_lat_sidebend",
                name = "Lat Side-Bend Stretch",
                durationSeconds = 60,
                instructions = "Stand, reach one arm overhead. Lean to the opposite side until you feel a deep stretch down your side. Hold 30s per side. Lengthens the lat which becomes shortened and tight after heavy rowing.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "pull_cd_bicep_doorframe",
                name = "Bicep Doorframe Stretch",
                durationSeconds = 60,
                instructions = "Place palm flat on a wall with arm extended at shoulder height, thumb pointing down. Rotate body away until a deep stretch is felt in the bicep and anterior shoulder. Hold 30s per side.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "pull_cd_child_pose",
                name = "Child's Pose",
                durationSeconds = 60,
                instructions = "Kneel and reach arms forward as far as possible, lowering chest toward the floor. Hold 60s. Decompresses the lumbar spine, stretches the lats, and calms the nervous system after heavy pulling.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "pull_cd_seated_twist",
                name = "Seated Spinal Twist",
                durationSeconds = 60,
                instructions = "Seated, cross one leg over. Rotate toward the raised knee using it as a lever. Hold 30s per side. Mobilises the thoracic spine and releases tension in the erector muscles after rows.",
                type = COOLDOWN
            )
        ),
        "legs" to listOf(
            WarmupCooldownItem(
                id = "legs_cd_quad_stretch",
                name = "Standing Quad Stretch",
                durationSeconds = 60,
                instructions = "Stand on one leg, pull the other heel to your glute. Keep knees together. Hold 30s per side. Essential after any squat work. Lengthens the rectus femoris through its full range.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "legs_cd_hamstring_seated",
                name = "Seated Hamstring Stretch",
                durationSeconds = 60,
                instructions = "Sit with legs extended. Hinge at hips and reach toward your feet without rounding the lower back. Hold 30s. Lengthens the hamstrings after RDLs and deadlifts. DO NOT round the spine.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "legs_cd_figure4_glute",
                name = "Figure-4 Glute Stretch",
                durationSeconds = 60,
                instructions = "Lie on back. Cross one ankle over the opposite knee. Pull both legs toward your chest. Hold 30s per side. Releases the piriformis and glute medius — the muscles that become tight and sore after hip thrust and squat sessions.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "legs_cd_hip_flexor",
                name = "Hip Flexor Lunge Stretch",
                durationSeconds = 60,
                instructions = "Step one foot forward into a deep lunge. Lower the rear knee to the floor. Lean into the front hip. Hold 30s per side. Quad and hip flexor dominant. Critical for maintaining upright posture.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "legs_cd_calf_wall",
                name = "Calf Wall Stretch",
                durationSeconds = 60,
                instructions = "Press one heel flat to the floor, toes on a wall. Hold 30s per side for the gastrocnemius. Then bend the knee slightly for the soleus. Both must be stretched after calf work.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "legs_cd_pigeon",
                name = "Pigeon Pose (Floor)",
                durationSeconds = 60,
                instructions = "From a push-up position, slide one knee forward behind the same-side wrist. Extend the opposite leg back. Sink hips toward the floor. Hold 30s per side. The deepest glute and hip external rotator stretch available without equipment.",
                type = COOLDOWN
            )
        ),
        "core_shoulders" to listOf(
            WarmupCooldownItem(
                id = "cs_cd_puppy_pose",
                name = "Puppy Pose",
                durationSeconds = 60,
                instructions = "From child's pose position, walk hands forward while keeping hips above knees. Let the chest melt toward the floor. Hold 60s. Deep lat and anterior shoulder stretch — perfect after overhead pressing and core work.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "cs_cd_thread_needle",
                name = "Thread-the-Needle (Thoracic)",
                durationSeconds = 60,
                instructions = "On all fours. Thread one arm under and through, lowering that shoulder to the floor. Hold 30s per side. Releases thoracic rotation restriction that builds up during overhead pressing and planks.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "cs_cd_neck_stretch",
                name = "Seated Neck Stretch",
                durationSeconds = 60,
                instructions = "Seated. Gently tilt head to one side, letting ear move toward shoulder. Hold 30s per side. Releases the upper trapezius and sternocleidomastoid — muscles that brace hard during all core and overhead work.",
                type = COOLDOWN
            ),
            WarmupCooldownItem(
                id = "cs_cd_overhead_reach",
                name = "Overhead Reach Stretch",
                durationSeconds = 60,
                instructions = "Seated or standing. Clasp hands overhead and push palms toward the ceiling. Hold 30s. Lengthens the lats, serratus, and triceps simultaneously. Then lean side to side to stretch each lat individually.",
                type = COOLDOWN
            )
        )
    )

    fun getWarmups(muscleGroupIds: List<String>): List<WarmupCooldownItem> =
        muscleGroupIds.flatMap { warmups[it] ?: emptyList() }.distinctBy { it.id }

    fun getCooldowns(muscleGroupIds: List<String>): List<WarmupCooldownItem> =
        muscleGroupIds.flatMap { cooldowns[it] ?: emptyList() }.distinctBy { it.id }
}
