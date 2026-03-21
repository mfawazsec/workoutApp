package com.workoutapp.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.data.model.WarmupCooldownItem
import com.workoutapp.data.seed.WarmupCooldownData
import com.workoutapp.ui.theme.Background
import com.workoutapp.ui.theme.CardSurface
import com.workoutapp.ui.theme.CardSurfaceElevated
import com.workoutapp.ui.theme.NeonCyan
import com.workoutapp.ui.theme.OnSurface
import com.workoutapp.ui.theme.OnSurfaceMuted

private val SUPERSET_LABELS = listOf("A", "B", "C", "D", "E", "F", "G", "H")

private val SUB_GROUP_LABELS = mapOf(
    "push_chest"      to "CHEST",
    "push_triceps"    to "TRICEPS",
    "pull_back"       to "BACK",
    "pull_biceps"     to "BICEPS",
    "legs_quads"      to "QUADS",
    "legs_hamstrings" to "HAMSTRINGS",
    "legs_glutes"     to "GLUTES",
    "legs_calves"     to "CALVES",
    "core_shoulders"  to "SHOULDERS",
    "core_stability"  to "CORE"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    muscleGroupIds: List<String>,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val viewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModel.Factory(application, muscleGroupIds)
    )

    val exerciseStates by viewModel.exercises.collectAsState()
    val groups = viewModel.muscleGroups
    val primaryColor = groups.firstOrNull()?.color ?: NeonCyan

    val warmups = remember(muscleGroupIds) { WarmupCooldownData.getWarmups(muscleGroupIds) }
    val cooldowns = remember(muscleGroupIds) { WarmupCooldownData.getCooldowns(muscleGroupIds) }

    // Group by subGroupId, preserving insertion order
    data class SubGroupSection(
        val subGroupId: String,
        val label: String,
        val color: Color,
        val states: List<ExerciseUiState>
    )

    val grouped = buildList<SubGroupSection> {
        val seen = mutableSetOf<String>()
        val bySubGroup = exerciseStates.groupBy { it.exercise.subGroupId }
        exerciseStates.forEach { state ->
            val sgId = state.exercise.subGroupId
            if (seen.add(sgId)) {
                val label = SUB_GROUP_LABELS[sgId] ?: sgId.uppercase()
                val color = MuscleGroup.fromId(state.exercise.muscleGroupId)?.color ?: NeonCyan
                add(SubGroupSection(sgId, label, color, bySubGroup[sgId]!!))
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        groups.forEach { group ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = group.color.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    group.displayName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = group.color,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = OnSurface)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.completeSession()
                        onNavigateBack()
                    }) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Finish",
                            tint = primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "DONE",
                            color = primaryColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    navigationIconContentColor = OnSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (exerciseStates.isEmpty()) {
                item {
                    Text(
                        "Loading exercises…",
                        color = OnSurfaceMuted,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                // ── Warmup section ─────────────────────────────────────────
                if (warmups.isNotEmpty()) {
                    item(key = "warmup_header") {
                        WarmupCooldownSection(
                            title = "WARM UP",
                            items = warmups,
                            accentColor = primaryColor.copy(alpha = 0.85f),
                            icon = { Icon(Icons.Default.LocalFireDepartment, null, tint = primaryColor, modifier = Modifier.size(16.dp)) }
                        )
                    }
                    item(key = "warmup_divider") { Spacer(Modifier.height(4.dp)) }
                }

                // ── Exercise sections ───────────────────────────────────────
                grouped.forEachIndexed { supersetIdx, section ->
                    val label = SUPERSET_LABELS.getOrElse(supersetIdx) { "${supersetIdx + 1}" }
                    val canAdd = viewModel.hasMoreExercises(section.subGroupId)
                    val canRemove = section.states.size > 1

                    item(key = "header_${section.subGroupId}") {
                        SupersetGroupHeader(
                            label = label,
                            subGroupLabel = section.label,
                            accentColor = section.color,
                            canAdd = canAdd,
                            canRemove = canRemove,
                            onAdd = { viewModel.addExercise(section.subGroupId) },
                            onRemove = { viewModel.removeExercise(section.subGroupId) }
                        )
                    }

                    itemsIndexed(
                        items = section.states,
                        key = { _, state -> state.exercise.id }
                    ) { cardIdx, state ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(state.exercise.id) { visible = true }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                tween(280, delayMillis = cardIdx * 60, easing = FastOutSlowInEasing)
                            ) + slideInVertically(
                                tween(280, delayMillis = cardIdx * 60, easing = FastOutSlowInEasing),
                                initialOffsetY = { it / 5 }
                            )
                        ) {
                            ExerciseCard(
                                exercise = state.exercise,
                                weightKg = state.weightKg,
                                reps = state.reps,
                                estimatedOneRepMaxKg = state.estimatedOneRepMaxKg,
                                overloadLog = state.overloadLog,
                                completedSets = state.completedSets,
                                alternatives = viewModel.getAlternatives(state.exercise),
                                onWeightChange = { newWeight, suggestedReps ->
                                    viewModel.updateWeight(state.exercise.id, newWeight, suggestedReps)
                                },
                                onRepsChange = { newReps ->
                                    viewModel.updateReps(state.exercise.id, newReps)
                                },
                                onSwap = { replacement ->
                                    viewModel.swapExercise(state.exercise, replacement)
                                },
                                onAddRep = {
                                    viewModel.logProgression(state.exercise.id, 1, 0, 0)
                                },
                                onAddEccentric = {
                                    viewModel.logProgression(state.exercise.id, 0, 1, 0)
                                },
                                onReduceRest = {
                                    viewModel.logProgression(state.exercise.id, 0, 0, 15)
                                },
                                onCompleteSet = {
                                    viewModel.completeNextSet(state.exercise.id)
                                },
                                onSetsChange = { newSets ->
                                    viewModel.updateSets(state.exercise.id, newSets)
                                }
                            )
                        }
                    }

                    if (supersetIdx < grouped.lastIndex) {
                        item(key = "divider_${section.subGroupId}") {
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color.Transparent,
                                                section.color.copy(alpha = 0.4f),
                                                section.color.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }

                // ── Cooldown section ─────────────────────────────────────────
                if (cooldowns.isNotEmpty()) {
                    item(key = "cooldown_divider") { Spacer(Modifier.height(4.dp)) }
                    item(key = "cooldown_header") {
                        WarmupCooldownSection(
                            title = "COOL DOWN",
                            items = cooldowns,
                            accentColor = NeonCyan.copy(alpha = 0.7f),
                            icon = { Icon(Icons.Default.AcUnit, null, tint = NeonCyan.copy(alpha = 0.7f), modifier = Modifier.size(16.dp)) }
                        )
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun WarmupCooldownSection(
    title: String,
    items: List<WarmupCooldownItem>,
    accentColor: Color,
    icon: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
        ) {
            icon()
            Text(
                title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 10.sp
                )
            )
        }
        items.forEach { item ->
            WarmupCooldownCard(item = item, accentColor = accentColor)
        }
    }
}

@Composable
private fun WarmupCooldownCard(
    item: WarmupCooldownItem,
    accentColor: Color
) {
    var expanded by rememberSaveable(item.id) { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header row — always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Coloured accent dot
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(accentColor)
                    )
                    Column {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            item.displayDuration,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor.copy(alpha = 0.75f),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = accentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }

            // Instruction bubble — animated expand
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(tween(200)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column {
                    HorizontalDivider(
                        color = accentColor.copy(alpha = 0.12f),
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                        color = accentColor.copy(alpha = 0.07f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.instructions,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnSurface,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupersetGroupHeader(
    label: String,
    subGroupLabel: String,
    accentColor: Color,
    canAdd: Boolean,
    canRemove: Boolean,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(accentColor.copy(alpha = 0.14f), Color.Transparent)
                )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "SUPERSET $label",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    )
                )
                Text(
                    subGroupLabel,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = OnSurface,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onRemove,
                    enabled = canRemove,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (canRemove) CardSurface else Color.Transparent)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Remove exercise",
                        tint = if (canRemove) accentColor else accentColor.copy(alpha = 0.25f),
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(
                    onClick = onAdd,
                    enabled = canAdd,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (canAdd) CardSurface else Color.Transparent)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add exercise",
                        tint = if (canAdd) accentColor else accentColor.copy(alpha = 0.25f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
