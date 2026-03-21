package com.workoutapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.*
import com.workoutapp.ui.workout.MuscleRegionIcons
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val HERO_DATE_FMT = DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartWorkout: (List<MuscleGroup>) -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenSuggest: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    trackingViewModel: TrackingViewModel = viewModel()
) {
    val todayTracking by trackingViewModel.todayTracking.collectAsState()
    val waterGoal by trackingViewModel.waterGoal.collectAsState()
    val suggestedGroups by homeViewModel.suggestedGroups.collectAsState()

    var showWaterPicker by remember { mutableStateOf(false) }
    var showCreatinePicker by remember { mutableStateOf(false) }
    var showMultivitaminPicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = OnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCalendar) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = OnSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Hero card ────────────────────────────────────────────────
            item {
                HeroCard(
                    suggestedGroups = suggestedGroups,
                    onStartSuggested = { onStartWorkout(suggestedGroups) },
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 20.dp)
                )
            }

            // ── Tracker rows ─────────────────────────────────────────────
            item {
                CompactTrackerCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = "WATER",
                    current = todayTracking.waterGlasses,
                    goal = waterGoal,
                    unit = "/ $waterGoal glasses",
                    accentColor = NeonCyan,
                    onIncrement = { trackingViewModel.addWater(1) },
                    onDecrement = { trackingViewModel.addWater(-1) },
                    onLongPress = { showWaterPicker = true }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CompactTrackerCard(
                        modifier = Modifier.weight(1f),
                        label = "CREATINE",
                        current = todayTracking.creatineServings * 3,
                        goal = 3,
                        unit = "g",
                        accentColor = NeonPurple,
                        onIncrement = { trackingViewModel.addCreatine(1) },
                        onDecrement = { trackingViewModel.addCreatine(-1) },
                        onLongPress = { showCreatinePicker = true }
                    )
                    CompactTrackerCard(
                        modifier = Modifier.weight(1f),
                        label = "MULTIVITAMIN",
                        current = todayTracking.multivitaminTaken,
                        goal = 1,
                        unit = "/ 1",
                        accentColor = NeonGreen,
                        onIncrement = { trackingViewModel.addMultivitamin(1) },
                        onDecrement = { trackingViewModel.addMultivitamin(-1) },
                        onLongPress = { showMultivitaminPicker = true }
                    )
                }
            }

            // ── Workouts header ──────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 28.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "WORKOUTS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = OnSurface,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                    )
                    Text(
                        "tap to start",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnSurfaceMuted,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            // ── Workout grid (2 × 2) ─────────────────────────────────────
            val groups = MuscleGroup.entries.toList()
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groups.getOrNull(0)?.let { g ->
                        WorkoutCard(group = g, modifier = Modifier.weight(1f), onClick = { onStartWorkout(listOf(g)) })
                    }
                    groups.getOrNull(1)?.let { g ->
                        WorkoutCard(group = g, modifier = Modifier.weight(1f), onClick = { onStartWorkout(listOf(g)) })
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groups.getOrNull(2)?.let { g ->
                        WorkoutCard(group = g, modifier = Modifier.weight(1f), onClick = { onStartWorkout(listOf(g)) })
                    }
                    groups.getOrNull(3)?.let { g ->
                        WorkoutCard(group = g, modifier = Modifier.weight(1f), onClick = { onStartWorkout(listOf(g)) })
                    }
                }
            }

            // ── Suggest button ───────────────────────────────────────────
            item {
                Button(
                    onClick = onOpenSuggest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "TODAY'S SUGGESTION",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    if (showWaterPicker) {
        NumberPickerBottomSheet(
            title = "Set Water Intake",
            current = todayTracking.waterGlasses,
            min = 0, max = 20,
            onConfirm = { value -> trackingViewModel.setWater(value); showWaterPicker = false },
            onDismiss = { showWaterPicker = false }
        )
    }
    if (showCreatinePicker) {
        NumberPickerBottomSheet(
            title = "Set Creatine Servings",
            current = todayTracking.creatineServings,
            min = 0, max = 5,
            onConfirm = { value -> trackingViewModel.setCreatine(value); showCreatinePicker = false },
            onDismiss = { showCreatinePicker = false }
        )
    }
    if (showMultivitaminPicker) {
        NumberPickerBottomSheet(
            title = "Set Multivitamin",
            current = todayTracking.multivitaminTaken,
            min = 0, max = 1,
            onConfirm = { value -> trackingViewModel.setMultivitamin(value); showMultivitaminPicker = false },
            onDismiss = { showMultivitaminPicker = false }
        )
    }
}

// ── Hero card ────────────────────────────────────────────────────────────────

@Composable
private fun HeroCard(
    suggestedGroups: List<MuscleGroup>,
    onStartSuggested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val heroResId = remember {
        context.resources.getIdentifier("ic_hero_atlas", "drawable", context.packageName)
    }
    val primaryColor = suggestedGroups.firstOrNull()?.color ?: NeonCyan
    val today = LocalDate.now().format(HERO_DATE_FMT).uppercase()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(BorderStroke(1.dp, primaryColor.copy(alpha = 0.35f)), RoundedCornerShape(24.dp))
    ) {
        // Background: image or gradient
        if (heroResId != 0) {
            Image(
                painter = painterResource(heroResId),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to (if (heroResId != 0) Color.Black.copy(alpha = 0.3f) else primaryColor.copy(alpha = 0.25f)),
                            0.45f to (if (heroResId != 0) Color.Black.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.7f)),
                            1.0f to Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        // Neon corner accent
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: date + label
            Column {
                Text(
                    today,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = primaryColor,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "GET\nAFTER IT.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        lineHeight = 38.sp
                    )
                )
            }

            // Bottom: suggested groups + button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "SUGGESTED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnSurfaceMuted,
                            letterSpacing = 1.5.sp
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        suggestedGroups.forEach { group ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = group.color.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, group.color.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    group.displayName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = group.color,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = onStartSuggested,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        "START",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

// ── Workout category card ─────────────────────────────────────────────────────

@Composable
private fun WorkoutCard(
    group: MuscleGroup,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageResName = "ic_workout_${group.id}"
    val imageResId = remember(imageResName) {
        context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    }
    val accentColor = group.color

    Box(
        modifier = modifier
            .height(175.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)), RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Background
        if (imageResId != 0) {
            Image(
                painter = painterResource(imageResId),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7f
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = if (imageResId != 0) arrayOf(
                            0.0f to Color.Black.copy(alpha = 0.25f),
                            0.5f to Color.Black.copy(alpha = 0.5f),
                            1.0f to Color.Black.copy(alpha = 0.88f)
                        ) else arrayOf(
                            0.0f to accentColor.copy(alpha = 0.18f),
                            1.0f to Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Top-left glow accent
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopStart)
                .background(
                    Brush.radialGradient(
                        listOf(accentColor.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Muscle region icons at the top
            MuscleRegionIcons(
                group = group,
                iconSize = 22.dp,
                spacing = 3.dp
            )

            // Name + subtitle at bottom
            Column {
                Text(
                    group.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    group.subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor.copy(alpha = 0.85f),
                        fontSize = 9.sp
                    )
                )
            }
        }

        // ChevronRight top-right
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = accentColor.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(16.dp)
        )
    }
}

// ── Compact tracker card ─────────────────────────────────────────────────────

@Composable
private fun CompactTrackerCard(
    modifier: Modifier = Modifier,
    label: String,
    current: Int,
    goal: Int,
    unit: String,
    accentColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onLongPress: () -> Unit
) {
    val progress = if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "progress"
    )
    val animatedCount by animateIntAsState(
        targetValue = current,
        animationSpec = tween(300),
        label = "count"
    )
    val isGoalMet = progress >= 1f

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = if (isGoalMet) 0.6f else 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (isGoalMet) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "$animatedCount",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = if (isGoalMet) accentColor else OnSurface,
                        fontWeight = FontWeight.Black
                    )
                )
                Text(
                    unit,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceMuted,
                        fontSize = 9.sp
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null,
                            tint = accentColor.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(accentColor.copy(alpha = 0.15f))
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = { onIncrement() }, onLongPress = { onLongPress() })
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null,
                            tint = accentColor, modifier = Modifier.size(14.dp))
                    }
                }
            }
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CardSurfaceElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Brush.horizontalGradient(listOf(accentColor.copy(0.6f), accentColor)))
                )
            }
        }
    }
}

// ── Number picker bottom sheet (unchanged) ───────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberPickerBottomSheet(
    title: String,
    current: Int,
    min: Int,
    max: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableIntStateOf(current) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = NeonCyan, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                IconButton(
                    onClick = { if (selected > min) selected-- },
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(CardSurfaceElevated)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = NeonCyan)
                }
                Text(
                    "$selected",
                    style = MaterialTheme.typography.displaySmall.copy(color = NeonCyan, fontWeight = FontWeight.Black),
                    modifier = Modifier.defaultMinSize(minWidth = 60.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { if (selected < max) selected++ },
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(CardSurfaceElevated)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NeonCyan)
                }
            }
            Button(
                onClick = { onConfirm(selected) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
