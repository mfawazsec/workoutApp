package com.workoutapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.*

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

    var showWaterPicker by remember { mutableStateOf(false) }
    var showCreatinePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ATLAS",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp,
                            color = NeonCyan
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = NeonCyan)
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tracker cards row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TrackerCard(
                        modifier = Modifier.weight(1f),
                        label = "WATER",
                        current = todayTracking.waterGlasses,
                        goal = waterGoal,
                        unit = "glasses",
                        accentColor = NeonCyan,
                        onIncrement = { trackingViewModel.addWater(1) },
                        onDecrement = { trackingViewModel.addWater(-1) },
                        onLongPress = { showWaterPicker = true }
                    )
                    TrackerCard(
                        modifier = Modifier.weight(1f),
                        label = "CREATINE",
                        current = todayTracking.creatineServings,
                        goal = 1,
                        unit = if (todayTracking.creatineServings == 1) "serving" else "servings",
                        accentColor = NeonPurple,
                        onIncrement = { trackingViewModel.addCreatine(1) },
                        onDecrement = { trackingViewModel.addCreatine(-1) },
                        onLongPress = { showCreatinePicker = true },
                        showGrams = true,
                        gramsPerServing = 3
                    )
                }
            }

            // Workouts header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "WORKOUTS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = OnSurface
                        )
                    )
                    IconButton(onClick = onOpenCalendar) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Calendar",
                            tint = NeonCyan
                        )
                    }
                }
            }

            // Workout type buttons
            items(MuscleGroup.entries.size) { index ->
                val group = MuscleGroup.entries[index]
                WorkoutTypeButton(
                    group = group,
                    onClick = { onStartWorkout(listOf(group)) }
                )
            }

            // SUGGEST button
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onOpenSuggest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, NeonCyan),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        "SUGGEST",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Water picker bottom sheet
    if (showWaterPicker) {
        NumberPickerBottomSheet(
            title = "Set Water Intake",
            current = todayTracking.waterGlasses,
            min = 0,
            max = 20,
            onConfirm = { value ->
                trackingViewModel.setWater(value)
                showWaterPicker = false
            },
            onDismiss = { showWaterPicker = false }
        )
    }

    // Creatine picker bottom sheet
    if (showCreatinePicker) {
        NumberPickerBottomSheet(
            title = "Set Creatine Servings",
            current = todayTracking.creatineServings,
            min = 0,
            max = 5,
            onConfirm = { value ->
                trackingViewModel.setCreatine(value)
                showCreatinePicker = false
            },
            onDismiss = { showCreatinePicker = false }
        )
    }
}

@Composable
private fun TrackerCard(
    modifier: Modifier = Modifier,
    label: String,
    current: Int,
    goal: Int,
    unit: String,
    accentColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onLongPress: () -> Unit,
    showGrams: Boolean = false,
    gramsPerServing: Int = 3
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

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = accentColor,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (showGrams) "${animatedCount * gramsPerServing}g" else "$animatedCount",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Black
                    )
                )
                Row {
                    // Minus button
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = accentColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    // Plus button with long-press
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor.copy(alpha = 0.15f))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onIncrement() },
                                    onLongPress = { onLongPress() }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Text(
                "$current / $goal $unit",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceMuted)
            )
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CardSurfaceElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(accentColor.copy(alpha = 0.7f), accentColor)
                            )
                        )
                )
            }
        }
    }
}

@Composable
private fun WorkoutTypeButton(
    group: MuscleGroup,
    onClick: () -> Unit
) {
    val accentColor = when (group.id) {
        "push" -> NeonOrange
        "pull" -> NeonBlue
        "legs" -> NeonGreen
        "core_shoulders" -> NeonPurple
        else -> NeonCyan
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                group.displayName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = OnSurface,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = accentColor
            )
        }
    }
}

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
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconButton(
                    onClick = { if (selected > min) selected-- },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardSurfaceElevated)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = NeonCyan)
                }
                Text(
                    "$selected",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Black
                    ),
                    modifier = Modifier.defaultMinSize(minWidth = 60.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { if (selected < max) selected++ },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardSurfaceElevated)
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
