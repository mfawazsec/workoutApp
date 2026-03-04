#!/usr/bin/env python3
"""
Workout PDF Generator
Creates a beautifully designed, landscape-oriented workout plan PDF.
Optimized for iPad and phone viewing.
"""

from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.colors import HexColor, white, black
from reportlab.lib.units import mm, inch
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_RIGHT
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle,
    PageBreak, KeepTogether
)
from reportlab.pdfgen import canvas
from reportlab.platypus.doctemplate import PageTemplate, BaseDocTemplate, Frame
import os

# ═══════════════════════════════════════════════════════════════
# COLOUR PALETTE
# ═══════════════════════════════════════════════════════════════
COLORS = {
    # Primary palette
    "bg_dark":       HexColor("#0F1117"),
    "bg_card":       HexColor("#1A1D27"),
    "bg_card_alt":   HexColor("#22252F"),
    "accent":        HexColor("#6C63FF"),  # Purple accent
    "accent_light":  HexColor("#8B83FF"),
    "text_primary":  HexColor("#EAEAEA"),
    "text_secondary":HexColor("#9BA1B0"),
    "text_muted":    HexColor("#6B7280"),
    "white":         HexColor("#FFFFFF"),
    "border":        HexColor("#2D3142"),

    # Day header colours
    "monday":    HexColor("#6C63FF"),  # Purple
    "tuesday":   HexColor("#00BFA6"),  # Teal
    "wednesday": HexColor("#FF6B6B"),  # Coral
    "thursday":  HexColor("#FFB347"),  # Amber
    "friday":    HexColor("#4FC3F7"),  # Sky blue

    # Muscle-group tag colours
    "chest":        HexColor("#FF6B6B"),
    "back":         HexColor("#4FC3F7"),
    "shoulders":    HexColor("#FFB347"),
    "biceps":       HexColor("#81C784"),
    "triceps":      HexColor("#BA68C8"),
    "quads":        HexColor("#FF8A65"),
    "hamstrings":   HexColor("#A1887F"),
    "glutes":       HexColor("#F06292"),
    "calves":       HexColor("#4DB6AC"),
    "core":         HexColor("#FFD54F"),
    "forearms":     HexColor("#7986CB"),
    "traps":        HexColor("#90A4AE"),
    "lats":         HexColor("#4DD0E1"),
    "hip_flexors":  HexColor("#CE93D8"),
    "full_body":    HexColor("#AED581"),
    "obliques":     HexColor("#FFAB91"),
    "lower_back":   HexColor("#BCAAA4"),
    "adductors":    HexColor("#80CBC4"),
    "abductors":    HexColor("#EF9A9A"),
    "rear_delts":   HexColor("#B0BEC5"),

    # Stretch / warm-up
    "warmup":  HexColor("#FF9800"),
    "cooldown":HexColor("#29B6F6"),

    # Status
    "rest":  HexColor("#4CAF50"),
    "caution": HexColor("#FFC107"),
}

# ═══════════════════════════════════════════════════════════════
# WORKOUT DATA
# ═══════════════════════════════════════════════════════════════

PROFILE = {
    "name": "Custom Hypertrophy Programme",
    "sex": "Male",
    "age": 26,
    "height": "5'10\" (178 cm)",
    "weight": "57.5 – 59 kg",
    "goal": "Hypertrophy & Definition",
    "frequency": "5 days / week  (Sat & Sun OFF)",
    "equipment": "1 × 10 kg Dumbbell  ·  1 × 5 kg Dumbbell  ·  Yoga Mat",
    "injury_note": "⚠ Shoulder dislocation history — all pressing & overhead movements are chosen for joint safety. Avoid end-range internal-rotation or unstable overhead positions.",
}

SPLIT_RATIONALE = (
    "This programme uses a scientifically-backed Upper / Lower / Push / Pull / Legs (ULPPL) "
    "split to maximise hypertrophy stimulus while respecting your shoulder history. Each muscle "
    "group is trained ~2× per week through direct and indirect work, which research shows is "
    "optimal for growth. All shoulder-intensive moves use neutral-grip or controlled ROM "
    "to avoid impingement and instability."
)

# Each day: { name, color_key, focus, warmup_stretches, exercises, cooldown_stretches }
# Each exercise: { name, equipment, sets_reps, tempo, rest, muscles[], steps[], note? }

DAYS = [
    # ─── MONDAY ──────────────────────────────────────────────
    {
        "day": "Monday",
        "title": "Lower Body — Quad & Glute Focus",
        "color_key": "monday",
        "warmup": [
            ("Leg Swings (front-to-back)", "20 each leg", ["hip_flexors", "hamstrings"]),
            ("Lateral Leg Swings", "15 each leg", ["adductors", "abductors"]),
            ("Bodyweight Squats", "15 reps", ["quads", "glutes"]),
            ("Walking Lunges (bodyweight)", "10 each leg", ["quads", "glutes"]),
            ("Ankle Circles", "10 each direction", ["calves"]),
        ],
        "exercises": [
            {
                "name": "Goblet Squat",
                "equipment": "10 kg DB",
                "sets_reps": "4 × 12",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["quads", "glutes", "core"],
                "steps": [
                    "Hold the 10 kg dumbbell vertically at chest height, cupping the top end with both palms.",
                    "Stand with feet shoulder-width apart, toes turned out ~15°.",
                    "Brace your core, push hips back and bend knees to descend.",
                    "Lower until thighs are at least parallel — elbows track inside the knees.",
                    "Drive through mid-foot to stand, squeezing glutes at the top.",
                ],
                "note": None,
            },
            {
                "name": "Dumbbell Romanian Deadlift (Single-Arm)",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 12 each side",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["hamstrings", "glutes", "lower_back", "core"],
                "steps": [
                    "Hold the 10 kg dumbbell in one hand, feet hip-width apart.",
                    "Soften the knee on the working side and hinge at the hips.",
                    "Lower the dumbbell along your shin, keeping your back flat.",
                    "Feel the stretch in your hamstring, then drive hips forward to stand.",
                    "Complete all reps on one side before switching.",
                ],
                "note": "Keep the non-working arm on your hip for balance.",
            },
            {
                "name": "Bulgarian Split Squat",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 10 each leg",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["quads", "glutes", "core"],
                "steps": [
                    "Place the top of your rear foot on a chair/couch behind you.",
                    "Hold the 5 kg dumbbell in the goblet position at chest height.",
                    "Lower your back knee toward the floor, bending the front knee to ~90°.",
                    "Keep your torso upright and front knee tracking over toes.",
                    "Push through the front heel to rise.",
                ],
                "note": "If balance is challenging, hold the DB at your side instead.",
            },
            {
                "name": "Dumbbell Sumo Squat",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 15",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["quads", "glutes", "adductors"],
                "steps": [
                    "Stand wide, toes pointed out ~45°. Hold the 10 kg dumbbell hanging between your legs.",
                    "Brace core and lower by bending knees outward.",
                    "Descend until thighs are parallel, keeping torso upright.",
                    "Drive through heels to stand, squeezing inner thighs and glutes at top.",
                ],
                "note": None,
            },
            {
                "name": "Single-Leg Glute Bridge (Weighted)",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 15 each leg",
                "tempo": "2-2-1-0",
                "rest": "45 s",
                "muscles": ["glutes", "hamstrings", "core"],
                "steps": [
                    "Lie on your back on the mat, knees bent, feet flat.",
                    "Place the 5 kg dumbbell on your hip crease.",
                    "Extend one leg straight out, keeping thighs parallel.",
                    "Drive through the planted heel, lifting hips until body forms a straight line.",
                    "Squeeze the glute hard at the top for 2 seconds, then lower.",
                ],
                "note": None,
            },
            {
                "name": "Standing Calf Raise (Single-Leg, Weighted)",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 20 each leg",
                "tempo": "2-1-1-0",
                "rest": "30 s",
                "muscles": ["calves"],
                "steps": [
                    "Stand on one foot on a step edge or flat ground, holding the 10 kg dumbbell on the same side.",
                    "Use a wall or door frame for balance with the other hand.",
                    "Rise onto your toes as high as possible, pausing at the peak.",
                    "Lower slowly, allowing a full stretch at the bottom.",
                ],
                "note": None,
            },
        ],
        "cooldown": [
            ("Standing Quad Stretch", "30 s each leg", ["quads"]),
            ("Seated Forward Fold", "45 s", ["hamstrings", "lower_back"]),
            ("Pigeon Pose", "45 s each side", ["glutes", "hip_flexors"]),
            ("Butterfly Stretch", "30 s", ["adductors"]),
            ("Calf Stretch against Wall", "30 s each leg", ["calves"]),
        ],
    },

    # ─── TUESDAY ─────────────────────────────────────────────
    {
        "day": "Tuesday",
        "title": "Upper Body — Pull (Back & Biceps)",
        "color_key": "tuesday",
        "warmup": [
            ("Arm Circles (small → large)", "15 each direction", ["shoulders"]),
            ("Cat-Cow Stretch", "10 reps", ["back", "core"]),
            ("Band Pull-Aparts (or towel pulls)", "15 reps", ["rear_delts", "traps"]),
            ("Scapular Squeezes", "15 reps, 2 s hold", ["traps", "rear_delts"]),
            ("Thoracic Rotations on Mat", "10 each side", ["back", "core"]),
        ],
        "exercises": [
            {
                "name": "Bent-Over Dumbbell Row (Neutral Grip)",
                "equipment": "10 kg DB",
                "sets_reps": "4 × 10 each arm",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["lats", "traps", "rear_delts", "biceps"],
                "steps": [
                    "Hinge at the hips ~45°, flat back. Support free hand on a chair.",
                    "Hold the 10 kg dumbbell with a neutral (palm-in) grip.",
                    "Pull the dumbbell toward your hip, driving the elbow past your torso.",
                    "Squeeze the shoulder blade in at the top for 1 s.",
                    "Lower under control for a 3-count.",
                ],
                "note": "Neutral grip keeps the shoulder in a safe position.",
            },
            {
                "name": "Prone Y-T-W Raises",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 8 (each letter)",
                "tempo": "2-2-2-0",
                "rest": "60 s",
                "muscles": ["traps", "rear_delts", "lower_back"],
                "steps": [
                    "Lie face-down on the mat, forehead resting on a towel.",
                    "Y: Raise both arms overhead at 45° (thumbs up), forming a Y-shape. Hold 2 s.",
                    "T: Raise arms straight out to sides (thumbs up), forming a T. Hold 2 s.",
                    "W: Bend elbows and pull them back, squeezing shoulder blades, forming a W. Hold 2 s.",
                    "Use the 5 kg dumbbell in the dominant hand, bodyweight on the other (or alternate sets).",
                ],
                "note": "⚠ Shoulder-safe: Stay below shoulder height. These build rotator-cuff stability.",
            },
            {
                "name": "Dumbbell Pullover (Floor)",
                "equipment": "10 kg DB + Mat",
                "sets_reps": "3 × 12",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["lats", "chest", "core"],
                "steps": [
                    "Lie on the mat, knees bent, feet flat. Hold the 10 kg dumbbell with both hands over your chest.",
                    "Keeping a slight bend in the elbows, slowly arc the weight overhead.",
                    "Lower until you feel a strong stretch in the lats (arms roughly in line with ears).",
                    "Pull the weight back to the start using your lats, not your arms.",
                ],
                "note": "⚠ Do NOT let the dumbbell go past your ears if it feels unstable in the shoulder.",
            },
            {
                "name": "Concentration Curl",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 12 each arm",
                "tempo": "2-1-3-0",
                "rest": "45 s",
                "muscles": ["biceps", "forearms"],
                "steps": [
                    "Sit on a chair/bench, spread knees apart.",
                    "Brace the back of your upper arm against your inner thigh.",
                    "Curl the 10 kg dumbbell toward your shoulder, squeezing the bicep.",
                    "Lower slowly (3-count negative) for maximum time under tension.",
                ],
                "note": None,
            },
            {
                "name": "Hammer Curl (Alternating)",
                "equipment": "5 kg DB + 10 kg DB",
                "sets_reps": "3 × 12 each arm",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["biceps", "forearms"],
                "steps": [
                    "Stand tall, 10 kg DB in dominant hand, 5 kg DB in non-dominant hand.",
                    "Keep palms facing each other (neutral grip) throughout.",
                    "Curl one arm up while keeping the elbow pinned to your side.",
                    "Lower under control, then curl the other arm.",
                ],
                "note": "Neutral grip is safer for shoulders and hits the brachialis for arm thickness.",
            },
            {
                "name": "Reverse Dumbbell Fly (Bent-Over)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 15",
                "tempo": "2-2-2-0",
                "rest": "45 s",
                "muscles": ["rear_delts", "traps", "back"],
                "steps": [
                    "Hinge forward at the hips ~60°, flat back.",
                    "Hold the 5 kg dumbbell in one hand (switch halfway or use both sets).",
                    "With a slight bend in the elbow, raise the arm out to the side.",
                    "Squeeze the rear delt and mid-trap at the top for 2 s.",
                    "Lower slowly. Complete reps, then switch arms.",
                ],
                "note": "⚠ Shoulder-safe: keep the movement below shoulder line.",
            },
        ],
        "cooldown": [
            ("Cross-Body Shoulder Stretch", "30 s each arm", ["shoulders", "rear_delts"]),
            ("Doorway Chest Stretch", "30 s each side", ["chest"]),
            ("Child's Pose", "45 s", ["lats", "lower_back"]),
            ("Biceps Wall Stretch", "30 s each arm", ["biceps"]),
            ("Thread the Needle", "30 s each side", ["traps", "back"]),
        ],
    },

    # ─── WEDNESDAY ───────────────────────────────────────────
    {
        "day": "Wednesday",
        "title": "Upper Body — Push (Chest & Triceps) ⚠ Shoulder-Safe",
        "color_key": "wednesday",
        "warmup": [
            ("Scapular Push-Ups on Mat", "12 reps", ["shoulders", "chest"]),
            ("Wall Slides (arms on wall)", "10 reps", ["shoulders", "traps"]),
            ("Arm Circles (small → large)", "15 each direction", ["shoulders"]),
            ("Thoracic Extension over rolled towel", "30 s", ["back"]),
            ("Wrist Circles", "10 each direction", ["forearms"]),
        ],
        "exercises": [
            {
                "name": "Floor Press (Neutral Grip)",
                "equipment": "10 kg DB + Mat",
                "sets_reps": "4 × 12 each arm",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["chest", "triceps"],
                "steps": [
                    "Lie on the mat, knees bent, feet flat on the floor.",
                    "Hold the 10 kg dumbbell in one hand with a neutral (palm-in) grip.",
                    "Press the dumbbell up until your arm is extended (don't lock the elbow).",
                    "Lower until your upper arm touches the mat — this limits ROM for shoulder safety.",
                    "Press back up, keeping your core tight and back flat.",
                    "Complete all reps, then switch arms.",
                ],
                "note": "⚠ Floor limits range of motion, removing the risky deep stretch on the shoulder.",
            },
            {
                "name": "Incline Push-Up (Hands Elevated)",
                "equipment": "Bodyweight",
                "sets_reps": "4 × 15",
                "tempo": "2-1-2-0",
                "rest": "60 s",
                "muscles": ["chest", "triceps", "core"],
                "steps": [
                    "Place hands on a sturdy chair or couch edge, wider than shoulder-width.",
                    "Walk feet back until body forms a straight line from head to heels.",
                    "Lower your chest toward the edge, keeping elbows at ~45° (NOT flared).",
                    "Push back up, squeezing the chest at the top.",
                ],
                "note": "⚠ Elbows at 45° protects the shoulder from impingement.",
            },
            {
                "name": "Svend Press (Squeeze Press)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 15",
                "tempo": "3-2-2-0",
                "rest": "45 s",
                "muscles": ["chest", "core"],
                "steps": [
                    "Stand or sit. Hold the 5 kg dumbbell between both palms at chest height.",
                    "Squeeze your palms together hard (isometric chest contraction).",
                    "Press the dumbbell forward until arms are extended.",
                    "Hold 2 s at full extension, keeping the squeeze constant.",
                    "Slowly bring it back to your chest.",
                ],
                "note": "This is a chest isolation move — no shoulder strain.",
            },
            {
                "name": "Diamond Push-Up (Knees if needed)",
                "equipment": "Mat",
                "sets_reps": "3 × 12",
                "tempo": "2-1-2-0",
                "rest": "60 s",
                "muscles": ["triceps", "chest", "core"],
                "steps": [
                    "On the mat, place hands close together forming a diamond shape under your chest.",
                    "Keep elbows tight to your body as you lower.",
                    "Descend until your chest nearly touches your hands.",
                    "Press up, focusing on the triceps squeeze.",
                    "Drop to knees if you cannot complete full reps with good form.",
                ],
                "note": None,
            },
            {
                "name": "Overhead Triceps Extension (Single-Arm, Seated)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 15 each arm",
                "tempo": "2-1-3-0",
                "rest": "45 s",
                "muscles": ["triceps"],
                "steps": [
                    "Sit on the floor with back supported against a wall.",
                    "Hold the 5 kg dumbbell in one hand, extend arm overhead.",
                    "Lower the dumbbell behind your head by bending the elbow.",
                    "Keep upper arm close to your ear and stationary.",
                    "Extend back up, squeezing the triceps at the top.",
                ],
                "note": "⚠ Using the lighter 5 kg dumbbell keeps the shoulder joint safe overhead.",
            },
            {
                "name": "Triceps Kickback (Bent-Over)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 15 each arm",
                "tempo": "2-2-2-0",
                "rest": "45 s",
                "muscles": ["triceps"],
                "steps": [
                    "Hinge at hips ~45°, brace free hand on a chair.",
                    "Hold the 5 kg dumbbell, upper arm parallel to torso.",
                    "Extend your forearm back until the arm is fully straight.",
                    "Squeeze the triceps hard for 2 s at full extension.",
                    "Slowly return to start.",
                ],
                "note": None,
            },
        ],
        "cooldown": [
            ("Doorway Chest Stretch", "30 s each side", ["chest"]),
            ("Overhead Triceps Stretch", "30 s each arm", ["triceps"]),
            ("Cross-Body Shoulder Stretch", "30 s each arm", ["shoulders"]),
            ("Child's Pose", "45 s", ["lats", "lower_back"]),
            ("Wrist Flexor/Extensor Stretch", "20 s each", ["forearms"]),
        ],
    },

    # ─── THURSDAY ─────────────────────────────────────────────
    {
        "day": "Thursday",
        "title": "Lower Body — Hamstring, Glute & Core Focus",
        "color_key": "thursday",
        "warmup": [
            ("Glute Bridges (bodyweight)", "15 reps", ["glutes", "hamstrings"]),
            ("Leg Swings (front-to-back)", "15 each leg", ["hip_flexors", "hamstrings"]),
            ("Inchworms", "8 reps", ["hamstrings", "core"]),
            ("Fire Hydrants", "12 each leg", ["glutes", "abductors"]),
            ("Dead Bug", "10 each side", ["core"]),
        ],
        "exercises": [
            {
                "name": "Dumbbell Hip Thrust (Both Legs)",
                "equipment": "10 kg DB + Mat",
                "sets_reps": "4 × 15",
                "tempo": "2-2-2-0",
                "rest": "60 s",
                "muscles": ["glutes", "hamstrings", "core"],
                "steps": [
                    "Sit on the floor, upper back against a couch/bench edge.",
                    "Place the 10 kg dumbbell on your hip crease, hold with both hands.",
                    "Drive through your heels, lifting hips until body is in a straight line.",
                    "Squeeze glutes hard at the top for a 2-s hold.",
                    "Lower slowly to the start.",
                ],
                "note": None,
            },
            {
                "name": "Dumbbell Stiff-Leg Deadlift (Both Hands)",
                "equipment": "10 kg DB",
                "sets_reps": "4 × 12",
                "tempo": "3-1-2-0",
                "rest": "60 s",
                "muscles": ["hamstrings", "glutes", "lower_back"],
                "steps": [
                    "Stand with feet hip-width apart, holding the 10 kg DB with both hands in front.",
                    "Keep legs nearly straight (soft knee) and hinge forward at the hips.",
                    "Lower the dumbbell along your shins until you feel a deep hamstring stretch.",
                    "Drive hips forward to return to standing, squeezing glutes.",
                ],
                "note": None,
            },
            {
                "name": "Curtsy Lunge (Weighted)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 12 each leg",
                "tempo": "2-1-2-0",
                "rest": "60 s",
                "muscles": ["glutes", "quads", "adductors"],
                "steps": [
                    "Hold the 5 kg dumbbell at chest height (goblet).",
                    "Step one foot behind and across your body, like a curtsy.",
                    "Lower until your front thigh is parallel to the ground.",
                    "Push through the front heel to return to standing.",
                ],
                "note": None,
            },
            {
                "name": "Dumbbell Lateral Lunge",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 10 each side",
                "tempo": "2-1-2-0",
                "rest": "60 s",
                "muscles": ["quads", "glutes", "adductors"],
                "steps": [
                    "Hold the 10 kg dumbbell at chest height (goblet).",
                    "Take a wide step to one side, bending that knee.",
                    "Push your hips back as you lower into the side lunge.",
                    "Keep the opposite leg straight. Push off to return to centre.",
                ],
                "note": None,
            },
            {
                "name": "Dead Bug (Weighted)",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 10 each side",
                "tempo": "3-1-3-0",
                "rest": "45 s",
                "muscles": ["core", "hip_flexors"],
                "steps": [
                    "Lie on the mat, arms extended toward the ceiling, knees at 90°.",
                    "Hold the 5 kg dumbbell between both hands above your chest.",
                    "Slowly extend one leg out straight while keeping your lower back flat.",
                    "Return to start and repeat on the other side.",
                    "Exhale as you extend, inhale as you return.",
                ],
                "note": "⚠ Shoulder-safe core work — no planks or side planks (injury trigger).",
            },
            {
                "name": "Pallof Press (Dumbbell on Floor Variant)",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 12 each side",
                "tempo": "2-2-2-0",
                "rest": "45 s",
                "muscles": ["core", "obliques"],
                "steps": [
                    "Kneel on the mat in a half-kneeling position.",
                    "Hold the 5 kg dumbbell at your chest with both hands.",
                    "Press the dumbbell straight out in front of you — resist the urge to rotate.",
                    "Hold 2 s, then slowly return to chest.",
                    "Complete reps on one side, then switch kneeling leg.",
                ],
                "note": "Anti-rotation core training — safe, functional, and effective.",
            },
        ],
        "cooldown": [
            ("Pigeon Pose", "45 s each side", ["glutes", "hip_flexors"]),
            ("Lying Hamstring Stretch", "30 s each leg", ["hamstrings"]),
            ("Supine Spinal Twist", "30 s each side", ["lower_back", "obliques"]),
            ("Child's Pose", "45 s", ["lower_back", "lats"]),
            ("Hip Flexor Lunge Stretch", "30 s each side", ["hip_flexors", "quads"]),
        ],
    },

    # ─── FRIDAY ──────────────────────────────────────────────
    {
        "day": "Friday",
        "title": "Arms, Shoulders (Safe) & Core",
        "color_key": "friday",
        "warmup": [
            ("Arm Circles (small → large)", "15 each direction", ["shoulders"]),
            ("Scapular Squeezes", "15 reps, 2 s hold", ["traps", "rear_delts"]),
            ("Cat-Cow Stretch", "10 reps", ["back", "core"]),
            ("Wrist Circles", "10 each direction", ["forearms"]),
            ("Dead Bug (bodyweight)", "8 each side", ["core"]),
        ],
        "exercises": [
            {
                "name": "Lateral Raise (Partial ROM)",
                "equipment": "5 kg DB",
                "sets_reps": "4 × 15 each arm",
                "tempo": "2-2-2-0",
                "rest": "45 s",
                "muscles": ["shoulders"],
                "steps": [
                    "Stand tall, hold the 5 kg dumbbell at your side.",
                    "Raise the arm out to the side — STOP at 60° (well below shoulder height).",
                    "Hold for 2 s, focusing on the medial deltoid contraction.",
                    "Lower slowly for a 2-count.",
                    "Complete reps, then switch arms.",
                ],
                "note": "⚠ Partial ROM (below 90°) eliminates impingement risk while still building delts.",
            },
            {
                "name": "Front Raise (Thumb-Up Grip)",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 12 each arm",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["shoulders", "core"],
                "steps": [
                    "Stand tall, hold the 5 kg dumbbell with thumb pointing up (neutral-ish grip).",
                    "Raise the arm forward to shoulder height — no higher.",
                    "Lower slowly and with control.",
                    "Complete reps, then switch arms.",
                ],
                "note": "⚠ Thumb-up grip keeps the shoulder externally rotated and safe.",
            },
            {
                "name": "Zottman Curl",
                "equipment": "5 kg DB",
                "sets_reps": "3 × 12 each arm",
                "tempo": "2-1-3-0",
                "rest": "45 s",
                "muscles": ["biceps", "forearms"],
                "steps": [
                    "Stand tall, curl the 5 kg DB with a supinated (palm-up) grip.",
                    "At the top, rotate your wrist so your palm faces down (pronated).",
                    "Lower slowly (3-count) with the pronated grip — this trains the forearms.",
                    "At the bottom, rotate palm back up and repeat.",
                ],
                "note": "Excellent for overall arm development — hits biceps AND forearms.",
            },
            {
                "name": "Cross-Body Hammer Curl",
                "equipment": "10 kg DB",
                "sets_reps": "3 × 10 each arm",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["biceps", "forearms"],
                "steps": [
                    "Stand tall, hold the 10 kg DB with neutral grip.",
                    "Curl the dumbbell across your body toward the opposite shoulder.",
                    "Squeeze at the top, then lower under control.",
                    "Keep your elbow pinned — only the forearm should move.",
                ],
                "note": None,
            },
            {
                "name": "Skull Crusher (Floor)",
                "equipment": "10 kg DB + Mat",
                "sets_reps": "3 × 12",
                "tempo": "2-1-3-0",
                "rest": "60 s",
                "muscles": ["triceps"],
                "steps": [
                    "Lie on the mat, hold the 10 kg DB with both hands.",
                    "Extend arms straight up over your chest.",
                    "Bend at the elbows, lowering the DB toward your forehead.",
                    "Keep upper arms vertical — only the forearms move.",
                    "Extend back up, squeezing the triceps.",
                ],
                "note": None,
            },
            {
                "name": "Bicycle Crunch (Slow & Controlled)",
                "equipment": "Mat",
                "sets_reps": "3 × 20 (total)",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["core", "obliques"],
                "steps": [
                    "Lie on the mat, hands behind your head (lightly — don't pull the neck).",
                    "Lift both shoulders off the mat.",
                    "Bring one knee toward your chest while rotating to touch the opposite elbow.",
                    "Slowly switch sides. Focus on full rotation, not speed.",
                ],
                "note": "⚠ No crunching the neck — fingertips behind ears, not clasped.",
            },
            {
                "name": "Weighted Reverse Crunch",
                "equipment": "5 kg DB + Mat",
                "sets_reps": "3 × 15",
                "tempo": "2-1-2-0",
                "rest": "45 s",
                "muscles": ["core", "hip_flexors"],
                "steps": [
                    "Lie on the mat, hold the 5 kg dumbbell between your feet/ankles.",
                    "Bend knees at 90°, arms flat on the floor for stability.",
                    "Curl your hips off the ground, bringing knees toward your chest.",
                    "Lower slowly — don't let your lower back arch excessively.",
                ],
                "note": None,
            },
        ],
        "cooldown": [
            ("Overhead Triceps Stretch", "30 s each arm", ["triceps"]),
            ("Cross-Body Shoulder Stretch", "30 s each arm", ["shoulders"]),
            ("Biceps Wall Stretch", "30 s each arm", ["biceps"]),
            ("Supine Spinal Twist", "30 s each side", ["lower_back", "obliques"]),
            ("Child's Pose", "60 s", ["full_body"]),
        ],
    },
]

WEEKEND = {
    "days": "Saturday & Sunday",
    "title": "Active Recovery & Rest",
    "suggestions": [
        "Light walking (20–40 min)",
        "Gentle yoga / mobility flow",
        "Foam rolling / self-myofascial release",
        "Full rest if fatigued",
        "Hydration and quality sleep (7–9 hrs)",
    ],
}


# ═══════════════════════════════════════════════════════════════
# PDF BUILDER
# ═══════════════════════════════════════════════════════════════

PAGE_W, PAGE_H = landscape(A4)   # ~842 × 595 pts

MARGIN_L  = 30 * mm
MARGIN_R  = 20 * mm
MARGIN_T  = 20 * mm
MARGIN_B  = 18 * mm
USABLE_W  = PAGE_W - MARGIN_L - MARGIN_R
USABLE_H  = PAGE_H - MARGIN_T - MARGIN_B


def draw_page_bg(canvas_obj, doc):
    """Draw a dark background on every page."""
    c = canvas_obj
    c.saveState()
    c.setFillColor(COLORS["bg_dark"])
    c.rect(0, 0, PAGE_W, PAGE_H, fill=True, stroke=False)

    # Subtle accent bar on the left
    c.setFillColor(COLORS["accent"])
    c.rect(0, 0, 8 * mm, PAGE_H, fill=True, stroke=False)

    # Footer line
    c.setStrokeColor(COLORS["border"])
    c.setLineWidth(0.5)
    c.line(MARGIN_L, MARGIN_B - 5 * mm, PAGE_W - MARGIN_R, MARGIN_B - 5 * mm)

    # Footer text
    c.setFont("Helvetica", 7)
    c.setFillColor(COLORS["text_muted"])
    c.drawString(MARGIN_L, MARGIN_B - 10 * mm, "Custom Hypertrophy Programme  •  Shoulder-Safe  •  Equipment: 10 kg DB · 5 kg DB · Yoga Mat")
    c.drawRightString(PAGE_W - MARGIN_R, MARGIN_B - 10 * mm, f"Page {doc.page}")

    c.restoreState()


def build_styles():
    """Create paragraph styles for the document."""
    styles = {}

    styles["title"] = ParagraphStyle(
        "Title",
        fontName="Helvetica-Bold",
        fontSize=28,
        leading=34,
        textColor=COLORS["white"],
        alignment=TA_LEFT,
        spaceAfter=4 * mm,
    )

    styles["subtitle"] = ParagraphStyle(
        "Subtitle",
        fontName="Helvetica",
        fontSize=13,
        leading=18,
        textColor=COLORS["text_secondary"],
        alignment=TA_LEFT,
        spaceAfter=2 * mm,
    )

    styles["section_header"] = ParagraphStyle(
        "SectionHeader",
        fontName="Helvetica-Bold",
        fontSize=18,
        leading=22,
        textColor=COLORS["white"],
        spaceAfter=3 * mm,
        spaceBefore=6 * mm,
    )

    styles["day_header"] = ParagraphStyle(
        "DayHeader",
        fontName="Helvetica-Bold",
        fontSize=22,
        leading=28,
        textColor=COLORS["white"],
        spaceAfter=2 * mm,
    )

    styles["body"] = ParagraphStyle(
        "Body",
        fontName="Helvetica",
        fontSize=10,
        leading=14,
        textColor=COLORS["text_primary"],
    )

    styles["body_small"] = ParagraphStyle(
        "BodySmall",
        fontName="Helvetica",
        fontSize=8.5,
        leading=12,
        textColor=COLORS["text_secondary"],
    )

    styles["exercise_name"] = ParagraphStyle(
        "ExerciseName",
        fontName="Helvetica-Bold",
        fontSize=12,
        leading=16,
        textColor=COLORS["white"],
    )

    styles["step"] = ParagraphStyle(
        "Step",
        fontName="Helvetica",
        fontSize=9,
        leading=13,
        textColor=COLORS["text_primary"],
        leftIndent=14,
        bulletIndent=0,
    )

    styles["note"] = ParagraphStyle(
        "Note",
        fontName="Helvetica-Oblique",
        fontSize=8.5,
        leading=12,
        textColor=COLORS["caution"],
        leftIndent=14,
    )

    styles["tag"] = ParagraphStyle(
        "Tag",
        fontName="Helvetica-Bold",
        fontSize=7.5,
        leading=10,
        textColor=COLORS["white"],
    )

    styles["stretch_name"] = ParagraphStyle(
        "StretchName",
        fontName="Helvetica",
        fontSize=9,
        leading=13,
        textColor=COLORS["text_primary"],
    )

    styles["profile_label"] = ParagraphStyle(
        "ProfileLabel",
        fontName="Helvetica-Bold",
        fontSize=9.5,
        leading=14,
        textColor=COLORS["text_secondary"],
    )

    styles["profile_value"] = ParagraphStyle(
        "ProfileValue",
        fontName="Helvetica",
        fontSize=10,
        leading=14,
        textColor=COLORS["white"],
    )

    return styles


def muscle_tag(muscle_name, styles):
    """Create a coloured muscle-group tag as HTML."""
    color = COLORS.get(muscle_name, COLORS["accent"])
    hex_col = color.hexval() if hasattr(color, 'hexval') else str(color)
    # Format the name nicely
    label = muscle_name.replace("_", " ").title()
    return f'<font color="{hex_col}"><b>▪ {label}</b></font>'


def build_cover_page(styles):
    """Build the cover / overview page."""
    elements = []
    elements.append(Spacer(1, 25 * mm))

    # Title
    elements.append(Paragraph(
        f'<font color="{COLORS["accent"].hexval()}">◆</font>  {PROFILE["name"]}',
        styles["title"]
    ))
    elements.append(Paragraph(
        "A personalised, shoulder-safe hypertrophy programme",
        styles["subtitle"]
    ))
    elements.append(Spacer(1, 8 * mm))

    # Profile card
    profile_data = [
        ("Age / Sex", f'{PROFILE["age"]}  ·  {PROFILE["sex"]}'),
        ("Height", PROFILE["height"]),
        ("Weight", PROFILE["weight"]),
        ("Goal", PROFILE["goal"]),
        ("Frequency", PROFILE["frequency"]),
        ("Equipment", PROFILE["equipment"]),
    ]

    for label, value in profile_data:
        elements.append(Paragraph(
            f'<font color="{COLORS["text_secondary"].hexval()}">{label}:</font>  '
            f'<font color="{COLORS["white"].hexval()}">{value}</font>',
            styles["body"]
        ))
        elements.append(Spacer(1, 1.5 * mm))

    elements.append(Spacer(1, 5 * mm))

    # Injury warning box
    elements.append(Paragraph(
        f'<font color="{COLORS["caution"].hexval()}"><b>⚠  SHOULDER SAFETY NOTE</b></font>',
        styles["body"]
    ))
    elements.append(Spacer(1, 1 * mm))
    elements.append(Paragraph(
        PROFILE["injury_note"],
        ParagraphStyle("InjuryNote", parent=styles["body"], textColor=COLORS["caution"], fontSize=9, leading=13)
    ))
    elements.append(Spacer(1, 6 * mm))

    # Split rationale
    elements.append(Paragraph(
        f'<font color="{COLORS["accent_light"].hexval()}"><b>PROGRAMME RATIONALE</b></font>',
        styles["body"]
    ))
    elements.append(Spacer(1, 1 * mm))
    elements.append(Paragraph(SPLIT_RATIONALE, styles["body_small"]))
    elements.append(Spacer(1, 8 * mm))

    # Weekly overview table
    elements.append(Paragraph(
        f'<font color="{COLORS["accent_light"].hexval()}"><b>WEEKLY OVERVIEW</b></font>',
        styles["body"]
    ))
    elements.append(Spacer(1, 3 * mm))

    overview_data = [
        [
            Paragraph(f'<b><font color="{COLORS["white"].hexval()}">Day</font></b>', styles["body_small"]),
            Paragraph(f'<b><font color="{COLORS["white"].hexval()}">Focus</font></b>', styles["body_small"]),
            Paragraph(f'<b><font color="{COLORS["white"].hexval()}">Duration</font></b>', styles["body_small"]),
        ]
    ]
    for day_data in DAYS:
        col = COLORS[day_data["color_key"]]
        overview_data.append([
            Paragraph(f'<font color="{col.hexval()}"><b>{day_data["day"]}</b></font>', styles["body_small"]),
            Paragraph(f'<font color="{COLORS["text_primary"].hexval()}">{day_data["title"]}</font>', styles["body_small"]),
            Paragraph(f'<font color="{COLORS["text_secondary"].hexval()}">~60–90 min</font>', styles["body_small"]),
        ])
    # Weekend
    overview_data.append([
        Paragraph(f'<font color="{COLORS["rest"].hexval()}"><b>Sat & Sun</b></font>', styles["body_small"]),
        Paragraph(f'<font color="{COLORS["text_primary"].hexval()}">Rest / Active Recovery</font>', styles["body_small"]),
        Paragraph(f'<font color="{COLORS["text_secondary"].hexval()}">—</font>', styles["body_small"]),
    ])

    overview_table = Table(overview_data, colWidths=[80, USABLE_W - 80 - 80, 80])
    overview_table.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), COLORS["bg_card"]),
        ("BACKGROUND", (0, 1), (-1, -1), COLORS["bg_card_alt"]),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [COLORS["bg_card_alt"], COLORS["bg_card"]]),
        ("TEXTCOLOR", (0, 0), (-1, -1), COLORS["text_primary"]),
        ("ALIGN", (0, 0), (-1, -1), "LEFT"),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("TOPPADDING", (0, 0), (-1, -1), 5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
        ("LEFTPADDING", (0, 0), (-1, -1), 8),
        ("LINEBELOW", (0, 0), (-1, 0), 1, COLORS["accent"]),
        ("LINEBELOW", (0, -1), (-1, -1), 0.5, COLORS["border"]),
    ]))
    elements.append(overview_table)

    return elements


def build_stretch_table(stretches, title, color, styles, table_width):
    """Build a coloured stretch table for warmup or cooldown."""
    elements = []
    elements.append(Paragraph(
        f'<font color="{color.hexval()}"><b>{title}</b></font>',
        styles["body"]
    ))
    elements.append(Spacer(1, 2 * mm))

    table_data = []
    for stretch_name, duration, muscles in stretches:
        tags = "  ".join([muscle_tag(m, styles) for m in muscles])
        table_data.append([
            Paragraph(f'<font color="{COLORS["text_primary"].hexval()}">{stretch_name}</font>', styles["stretch_name"]),
            Paragraph(f'<font color="{COLORS["text_secondary"].hexval()}">{duration}</font>', styles["body_small"]),
            Paragraph(tags, styles["body_small"]),
        ])

    col1_w = table_width * 0.40
    col2_w = table_width * 0.20
    col3_w = table_width * 0.40
    t = Table(table_data, colWidths=[col1_w, col2_w, col3_w])
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), COLORS["bg_card"]),
        ("ROWBACKGROUNDS", (0, 0), (-1, -1), [COLORS["bg_card"], COLORS["bg_card_alt"]]),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("TOPPADDING", (0, 0), (-1, -1), 4),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
        ("LEFTPADDING", (0, 0), (-1, -1), 6),
        ("LINEBELOW", (0, 0), (-1, -2), 0.3, COLORS["border"]),
        ("LINEBEFORE", (0, 0), (0, -1), 2, color),
    ]))
    elements.append(t)
    return elements


def build_exercise_block(ex, idx, day_color, styles, table_width):
    """Build a styled block for one exercise."""
    elements = []

    # Exercise header with number
    color_hex = day_color.hexval()
    header_text = (
        f'<font color="{color_hex}" size="14"><b>{idx}.</b></font>  '
        f'<font color="{COLORS["white"].hexval()}" size="12"><b>{ex["name"]}</b></font>'
    )
    elements.append(Paragraph(header_text, styles["exercise_name"]))
    elements.append(Spacer(1, 1 * mm))

    # Meta row: equipment | sets × reps | tempo | rest
    meta_parts = []
    if ex["equipment"]:
        meta_parts.append(f'<font color="{COLORS["accent_light"].hexval()}">⬡ {ex["equipment"]}</font>')
    meta_parts.append(f'<font color="{COLORS["text_primary"].hexval()}">{ex["sets_reps"]}</font>')
    meta_parts.append(f'<font color="{COLORS["text_secondary"].hexval()}">Tempo: {ex["tempo"]}</font>')
    meta_parts.append(f'<font color="{COLORS["text_secondary"].hexval()}">Rest: {ex["rest"]}</font>')
    meta_line = "    ·    ".join(meta_parts)
    elements.append(Paragraph(meta_line, styles["body_small"]))
    elements.append(Spacer(1, 1.5 * mm))

    # Muscle tags
    tags = "    ".join([muscle_tag(m, styles) for m in ex["muscles"]])
    elements.append(Paragraph(tags, styles["body_small"]))
    elements.append(Spacer(1, 2 * mm))

    # Steps
    for i, step in enumerate(ex["steps"], 1):
        step_text = f'<font color="{color_hex}"><b>{i}.</b></font>  {step}'
        elements.append(Paragraph(step_text, styles["step"]))
        elements.append(Spacer(1, 0.5 * mm))

    # Note
    if ex.get("note"):
        elements.append(Spacer(1, 1 * mm))
        elements.append(Paragraph(ex["note"], styles["note"]))

    elements.append(Spacer(1, 4 * mm))

    # Separator line using a thin table
    sep = Table([[""]],colWidths=[table_width])
    sep.setStyle(TableStyle([
        ("LINEBELOW", (0, 0), (-1, -1), 0.3, COLORS["border"]),
        ("TOPPADDING", (0, 0), (-1, -1), 0),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 0),
    ]))
    elements.append(sep)
    elements.append(Spacer(1, 3 * mm))

    return elements


def build_day_pages(day_data, styles):
    """Build all elements for one training day."""
    elements = []
    day_color = COLORS[day_data["color_key"]]

    # Day header
    elements.append(Spacer(1, 6 * mm))
    elements.append(Paragraph(
        f'<font color="{day_color.hexval()}" size="26">◆</font>  '
        f'<font color="{day_color.hexval()}" size="13">{day_data["day"].upper()}</font>',
        styles["day_header"]
    ))
    elements.append(Paragraph(
        f'<font color="{COLORS["white"].hexval()}">{day_data["title"]}</font>',
        styles["section_header"]
    ))
    elements.append(Spacer(1, 4 * mm))

    # Warm-up
    wu = build_stretch_table(day_data["warmup"], "🔥  WARM-UP  (5–8 minutes)", COLORS["warmup"], styles, USABLE_W)
    elements.extend(wu)
    elements.append(Spacer(1, 6 * mm))

    # Exercises header
    elements.append(Paragraph(
        f'<font color="{day_color.hexval()}"><b>▬▬  EXERCISES  ▬▬</b></font>',
        styles["body"]
    ))
    elements.append(Spacer(1, 4 * mm))

    # Exercises
    for i, ex in enumerate(day_data["exercises"], 1):
        ex_elements = build_exercise_block(ex, i, day_color, styles, USABLE_W)
        elements.extend(ex_elements)

    # Cool-down
    cd = build_stretch_table(day_data["cooldown"], "❄  COOL-DOWN  (5–8 minutes)", COLORS["cooldown"], styles, USABLE_W)
    elements.extend(cd)

    return elements


def build_weekend_page(styles):
    """Build the weekend / rest page."""
    elements = []
    elements.append(Spacer(1, 15 * mm))
    elements.append(Paragraph(
        f'<font color="{COLORS["rest"].hexval()}" size="26">◆</font>  '
        f'<font color="{COLORS["rest"].hexval()}" size="13">SATURDAY  &  SUNDAY</font>',
        styles["day_header"]
    ))
    elements.append(Paragraph(
        f'<font color="{COLORS["white"].hexval()}">Active Recovery & Rest</font>',
        styles["section_header"]
    ))
    elements.append(Spacer(1, 6 * mm))

    elements.append(Paragraph(
        "Rest days are critical for muscle growth. Your muscles repair and grow during recovery. "
        "Use these days for light activity that promotes blood flow without causing fatigue.",
        styles["body"]
    ))
    elements.append(Spacer(1, 6 * mm))

    for suggestion in WEEKEND["suggestions"]:
        elements.append(Paragraph(
            f'<font color="{COLORS["rest"].hexval()}">✓</font>  {suggestion}',
            styles["body"]
        ))
        elements.append(Spacer(1, 2 * mm))

    elements.append(Spacer(1, 10 * mm))

    # Nutrition tips
    elements.append(Paragraph(
        f'<font color="{COLORS["accent_light"].hexval()}"><b>NUTRITION NOTES</b></font>',
        styles["section_header"]
    ))
    nutrition_tips = [
        "<b>Protein</b>: Aim for 1.6–2.2 g/kg bodyweight daily (~95–130 g). Space across 4–5 meals.",
        "<b>Calories</b>: You're underweight for your height. A slight caloric surplus (+200–400 kcal) is essential for muscle growth.",
        "<b>Creatine</b>: 3–5 g monohydrate daily is the most studied supplement for hypertrophy. Safe and effective.",
        "<b>Hydration</b>: 2.5–3.5 litres daily. More on training days.",
        "<b>Sleep</b>: 7–9 hours. Growth hormone peaks during deep sleep.",
    ]
    for tip in nutrition_tips:
        elements.append(Paragraph(
            f'<font color="{COLORS["accent"].hexval()}">▸</font>  {tip}',
            styles["body"]
        ))
        elements.append(Spacer(1, 2 * mm))

    elements.append(Spacer(1, 10 * mm))

    # Progression note
    elements.append(Paragraph(
        f'<font color="{COLORS["accent_light"].hexval()}"><b>PROGRESSIVE OVERLOAD</b></font>',
        styles["section_header"]
    ))
    prog_tips = [
        "When you can complete all sets & reps with good form, progress by:",
        "<b>1.</b>  Adding 1–2 reps per set",
        "<b>2.</b>  Adding a slow eccentric (e.g. 4-s lowering)",
        "<b>3.</b>  Adding a pause (1–2 s) at peak contraction",
        "<b>4.</b>  Reducing rest periods by 10–15 s",
        "<b>5.</b>  When ready, invest in heavier dumbbells (adjustable is best)",
    ]
    for tip in prog_tips:
        elements.append(Paragraph(
            f'<font color="{COLORS["accent"].hexval()}">▸</font>  {tip}',
            styles["body"]
        ))
        elements.append(Spacer(1, 2 * mm))

    # Tempo explanation
    elements.append(Spacer(1, 10 * mm))
    elements.append(Paragraph(
        f'<font color="{COLORS["accent_light"].hexval()}"><b>READING THE TEMPO</b></font>',
        styles["section_header"]
    ))
    elements.append(Spacer(1, 2 * mm))
    tempo_text = (
        'Tempo is written as <b>4 digits</b> (e.g. <b>3-1-2-0</b>):<br/>'
        '<font color="{c}"><b>1st</b></font> = Eccentric (lowering) in seconds  ·  '
        '<font color="{c}"><b>2nd</b></font> = Pause at bottom  ·  '
        '<font color="{c}"><b>3rd</b></font> = Concentric (lifting)  ·  '
        '<font color="{c}"><b>4th</b></font> = Pause at top'
    ).format(c=COLORS["accent_light"].hexval())
    elements.append(Paragraph(tempo_text, styles["body"]))

    return elements


def generate_pdf(output_path="Workout_Plan.pdf"):
    """Main function to generate the PDF."""
    doc = SimpleDocTemplate(
        output_path,
        pagesize=landscape(A4),
        leftMargin=MARGIN_L,
        rightMargin=MARGIN_R,
        topMargin=MARGIN_T,
        bottomMargin=MARGIN_B,
        title=PROFILE["name"],
        author="Workout PDF Generator",
        subject="Hypertrophy Training Programme — Shoulder Safe",
    )

    styles = build_styles()
    story = []

    # Cover page
    story.extend(build_cover_page(styles))
    story.append(PageBreak())

    # Each day gets its own section
    for day_data in DAYS:
        story.extend(build_day_pages(day_data, styles))
        story.append(PageBreak())

    # Weekend / recovery / nutrition
    story.extend(build_weekend_page(styles))

    # Build the PDF
    doc.build(story, onFirstPage=draw_page_bg, onLaterPages=draw_page_bg)
    print(f"\n✅  PDF generated: {os.path.abspath(output_path)}")
    print(f"    Pages: landscape A4, optimised for iPad & phone viewing")
    print(f"    Size:  {os.path.getsize(output_path) / 1024:.0f} KB\n")


if __name__ == "__main__":
    generate_pdf()
