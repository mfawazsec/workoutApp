package com.workoutapp.ui.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.SwapHoriz
import com.workoutapp.ui.theme.NeonGreen
import com.workoutapp.ui.theme.NeonOrange
import com.workoutapp.ui.theme.NeonPurple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutapp.data.model.Exercise
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.CardSurface
import com.workoutapp.ui.theme.CardSurfaceElevated
import com.workoutapp.ui.theme.NeonCyan
import com.workoutapp.ui.theme.OnSurface
import com.workoutapp.ui.theme.OnSurfaceMuted
import com.workoutapp.ui.theme.OnSurfaceVariant

private val BOLD_KEYWORDS = setOf(
    "elbows", "elbow", "shoulder", "shoulders", "chest", "back",
    "core", "hips", "hip", "knees", "knee", "wrist", "spine", "head",
    "neck", "glutes", "quads", "lats", "lat", "biceps", "triceps",
    "neutral", "flat", "straight", "parallel", "perpendicular",
    "floor", "bed", "brace", "squeeze", "pause", "slow",
    "full", "range", "stretch", "peak", "top", "bottom"
)

@Composable
fun ExerciseCard(
    exercise: Exercise,
    weightKg: Float,
    reps: Int,
    estimatedOneRepMaxKg: Float,
    overloadLog: OverloadLog?,
    alternatives: List<Exercise>,
    onWeightChange: (Float, Int) -> Unit,
    onRepsChange: (Int) -> Unit,
    onSwap: (Exercise) -> Unit,
    onAddRep: () -> Unit,
    onAddEccentric: () -> Unit,
    onReduceRest: () -> Unit,
    onCompleteSet: () -> Unit,
    onSetsChange: (Int) -> Unit,
    completedSets: Int = 0,
    modifier: Modifier = Modifier
) {
    val group = MuscleGroup.fromId(exercise.muscleGroupId)
    val accentColor = group?.color ?: MaterialTheme.colorScheme.primary
    val isDone = completedSets >= exercise.sets

    var isExpanded by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showSwapSheet by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isDone     -> NeonCyan.copy(alpha = 0.55f)
            isExpanded -> accentColor.copy(alpha = 0.5f)
            else       -> accentColor.copy(alpha = 0.22f)
        },
        animationSpec = tween(250),
        label = "borderColor"
    )

    val weightLabel = when {
        weightKg == 0f -> "BW"
        weightKg == weightKg.toLong().toFloat() -> "${weightKg.toLong()} kg"
        else -> "${"%.1f".format(weightKg)} kg"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)) {

            // ── Header row ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Accent stripe
                Box(
                    modifier = Modifier
                        .size(width = 3.dp, height = 40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(accentColor)
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    )
                    Text(
                        text = exercise.equipment,
                        style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceMuted)
                    )
                    // Per-muscle Ripperdoc icon row
                    if (group != null) {
                        Spacer(Modifier.height(6.dp))
                        MuscleRegionIcons(
                            group = group,
                            iconSize = 26.dp,
                            spacing = 4.dp
                        )
                    }
                }
                // Per-set circles — tap to complete next set
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable(enabled = !isDone) { onCompleteSet() }
                ) {
                    val setColors = listOf(accentColor, NeonCyan, NeonPurple, NeonOrange, NeonGreen)
                    repeat(exercise.sets) { idx ->
                        val filled = idx < completedSets
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(
                                    if (filled) setColors[idx % setColors.size]
                                    else OnSurfaceMuted.copy(alpha = 0.25f)
                                )
                        )
                    }
                }
                // Swap icon
                IconButton(
                    onClick = { showSwapSheet = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.SwapHoriz,
                        contentDescription = "Swap exercise",
                        tint = accentColor.copy(alpha = 0.75f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Expand toggle
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // ── Stats row: Sets · Weight · Reps ──────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sets stepper
                SetsStepperControl(
                    sets = exercise.sets,
                    onSetsChange = onSetsChange,
                    accentColor = accentColor
                )

                // Weight — tappable pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CardSurfaceElevated,
                    modifier = Modifier.clickable { showWeightDialog = true }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            weightLabel,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Text(
                            "tap weight",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Reps / seconds stepper
                RepsStepperControl(
                    reps = reps,
                    onRepsChange = onRepsChange,
                    accentColor = accentColor,
                    isTimeBased = exercise.isTimeBased
                )
            }

            // ── Expanded section ──────────────────────────────────────────
            if (isExpanded) {
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = accentColor.copy(alpha = 0.12f))
                Spacer(Modifier.height(14.dp))

                if (exercise.tempo != null) {
                    TempoVisualizer(exercise.tempo, accentColor)
                    Spacer(Modifier.height(14.dp))
                }

                // Step-by-step bullet instructions with bold keywords
                if (exercise.notes.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = accentColor.copy(alpha = 0.07f)
                    ) {
                        Text(
                            text = buildInstructionText(exercise.notes, accentColor),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnSurface,
                                lineHeight = 20.sp
                            )
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                }

                OverloadTracker(
                    log = overloadLog,
                    accentColor = accentColor,
                    onAddRep = onAddRep,
                    onAddEccentric = onAddEccentric,
                    onReduceRest = onReduceRest
                )
                Spacer(Modifier.height(14.dp))

                val targetRest = maxOf(0, exercise.restSeconds - (overloadLog?.reducedRestSeconds ?: 0))
                FluidRestTimer(targetRest, accentColor)
                Spacer(Modifier.height(4.dp))
            }
        }
    }

    if (showWeightDialog) {
        WeightInputDialog(
            currentWeightKg = weightKg,
            currentReps = reps,
            estimatedOneRepMaxKg = estimatedOneRepMaxKg,
            accentColor = accentColor,
            onConfirm = { newWeight, suggestedReps ->
                onWeightChange(newWeight, suggestedReps)
                showWeightDialog = false
            },
            onDismiss = { showWeightDialog = false }
        )
    }

    if (showSwapSheet) {
        ExerciseSwapSheet(
            currentExercise = exercise,
            alternatives = alternatives,
            onSelect = { replacement ->
                onSwap(replacement)
                showSwapSheet = false
            },
            onDismiss = { showSwapSheet = false }
        )
    }
}

/** Parse notes into bullet-point AnnotatedString with bolded coaching keywords. */
private fun buildInstructionText(notes: String, accentColor: Color) = buildAnnotatedString {
    val steps = notes.split(Regex("""[.;]\s+""")).map { it.trim() }.filter { it.isNotBlank() }
    steps.forEachIndexed { idx, step ->
        if (idx > 0) append("\n")
        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) { append("• ") }
        step.split(" ").forEachIndexed { wi, word ->
            if (wi > 0) append(" ")
            val clean = word.lowercase().trimEnd('.', ',', '°', '!', '?', ':')
            val isKeyword = clean in BOLD_KEYWORDS || word.any { it.isDigit() }
            if (isKeyword) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = OnSurface)) { append(word) }
            } else {
                append(word)
            }
        }
    }
}
