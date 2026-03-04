package com.workoutapp.ui.workout

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutapp.data.model.Exercise
import com.workoutapp.data.model.MuscleGroup

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
    modifier: Modifier = Modifier
) {
    val group = MuscleGroup.fromId(exercise.muscleGroupId)
    val accentColor = group?.color ?: MaterialTheme.colorScheme.primary

    var isExpanded by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showSwapSheet by remember { mutableStateOf(false) }

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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)) {

            // ── Header ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Accent stripe
                Box(
                    modifier = Modifier
                        .size(width = 3.dp, height = 36.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = exercise.equipment,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Swap icon — small
                IconButton(
                    onClick = { showSwapSheet = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.SwapHoriz,
                        contentDescription = "Swap exercise",
                        tint = accentColor.copy(alpha = 0.8f),
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Stats row: Sets · Weight · Reps ──────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp),   // align under text, past stripe
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sets badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${exercise.sets}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Text(
                            "sets",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor.copy(alpha = 0.7f)
                        )
                    }
                }

                // Weight — tappable pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.08f),
                    modifier = Modifier.clickable { showWeightDialog = true }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            weightLabel,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Text(
                            "tap weight",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor.copy(alpha = 0.5f)
                        )
                    }
                }

                // Reps stepper — compact
                RepsStepperControl(
                    reps = reps,
                    onRepsChange = onRepsChange,
                    accentColor = accentColor
                )
            }

            // ── Expanded detail ───────────────────────────────────────────
            if (isExpanded) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    color = accentColor.copy(alpha = 0.12f)
                )
                Spacer(modifier = Modifier.height(14.dp))

                if (exercise.tempo != null) {
                    TempoVisualizer(exercise.tempo, accentColor)
                    Spacer(modifier = Modifier.height(14.dp))
                }

                if (exercise.notes.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = accentColor.copy(alpha = 0.07f)
                    ) {
                        Text(
                            exercise.notes,
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                OverloadTracker(
                    log = overloadLog,
                    accentColor = accentColor,
                    onAddRep = onAddRep,
                    onAddEccentric = onAddEccentric,
                    onReduceRest = onReduceRest
                )
                Spacer(modifier = Modifier.height(14.dp))

                val targetRest = maxOf(0, exercise.restSeconds - (overloadLog?.reducedRestSeconds ?: 0))
                FluidRestTimer(targetRest, accentColor)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    // Dialogs
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
