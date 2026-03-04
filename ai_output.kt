@file:Suppress("MagicNumber", "UnusedPrivateProperty", "LongMethod", "WildcardImport", "FunctionNaming", "CyclomaticComplexMethod", "TooManyFunctions", "MaxLineLength", "MatchingDeclarationName")
package com.workoutapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// --- Data Classes ---

data class Tempo(
    val eccentric: Int,
    val bottomPause: Int,
    val concentric: Int,
    val topPause: Int
) {
    override fun toString(): String = "$eccentric-$bottomPause-$concentric-$topPause"
}

data class Exercise(
    val id: String,
    val name: String,
    val equipment: String,
    val sets: Int,
    val reps: Int,
    val weight: String,
    val tempo: Tempo?,
    val isShoulderSafe: Boolean,
    val restSeconds: Int = 60
)

data class WorkoutDay(
    val id: String,
    val name: String,
    val description: String,
    val exercises: List<Exercise>,
    val isActiveRecovery: Boolean = false
)

data class ProgressionLog(
    val exerciseId: String,
    val addedReps: Int = 0,
    val addedEccentricSeconds: Int = 0,
    val reducedRestSeconds: Int = 0
)

// --- ViewModel ---

class WorkoutViewModel : ViewModel() {
    private val dbExercises = listOf(
        Exercise("1", "Goblet Squat", "10 kg DB", 4, 12, "10 kg", Tempo(3, 1, 2, 0), isShoulderSafe = true, restSeconds = 60),
        Exercise("2", "Bent-Over Dumbbell Row", "10 kg DB", 4, 10, "10 kg", Tempo(3, 1, 2, 0), isShoulderSafe = true, restSeconds = 60),
        Exercise("3", "Floor Press", "10 kg DB", 4, 12, "10 kg", null, isShoulderSafe = false, restSeconds = 60),
        Exercise("4", "Dumbbell RDL", "10 kg DB", 4, 10, "10 kg", Tempo(3, 1, 2, 0), isShoulderSafe = true, restSeconds = 45),
        Exercise("5", "Reverse Lunge", "5 kg DB", 3, 12, "5 kg", Tempo(2, 0, 2, 0), isShoulderSafe = true, restSeconds = 45),
        Exercise("6", "Push Up", "Yoga Mat", 3, 15, "Bodyweight", Tempo(2, 1, 2, 0), isShoulderSafe = true, restSeconds = 60),
        Exercise("7", "Lateral Raise", "5 kg DB", 3, 15, "5 kg", Tempo(2, 0, 2, 0), isShoulderSafe = false, restSeconds = 45)
    )

    private val _workoutSplit = MutableStateFlow(
        listOf(
            WorkoutDay("d1", "Monday", "Lower Body", listOf(dbExercises[0])),
            WorkoutDay("d2", "Tuesday", "Upper Pull", listOf(dbExercises[1])),
            WorkoutDay("d3", "Wednesday", "Upper Push", listOf(dbExercises[2])),
            WorkoutDay("d4", "Thursday", "Legs", listOf(dbExercises[3], dbExercises[4])),
            WorkoutDay("d5", "Friday", "Full Body", listOf(dbExercises[0], dbExercises[1])),
            WorkoutDay("d6", "Saturday", "Active Recovery", emptyList(), isActiveRecovery = true),
            WorkoutDay("d7", "Sunday", "Active Recovery", emptyList(), isActiveRecovery = true)
        )
    )
    val workoutSplit: StateFlow<List<WorkoutDay>> = _workoutSplit.asStateFlow()

    private val _currentDay = MutableStateFlow(_workoutSplit.value[0])
    val currentDay = _currentDay.asStateFlow()

    private val _progressionLogs = MutableStateFlow<Map<String, ProgressionLog>>(emptyMap())
    val progressionLogs = _progressionLogs.asStateFlow()

    fun selectDay(day: WorkoutDay) {
        _currentDay.value = day
    }

    // Safety Constraint Engine: Filter only shoulder-safe alternatives
    fun getReplacementOptions(currentExerciseId: String): List<Exercise> {
        return dbExercises.filter { it.isShoulderSafe && it.id != currentExerciseId }
    }

    fun swapExercise(oldExercise: Exercise, newExercise: Exercise) {
        val updatedSplit = _workoutSplit.value.map { day ->
            if (day.id == _currentDay.value.id) {
                day.copy(exercises = day.exercises.map {
                    if (it.id == oldExercise.id) newExercise else it
                })
            } else day
        }
        _workoutSplit.value = updatedSplit
        selectDay(updatedSplit.find { it.id == _currentDay.value.id }!!)
    }

    fun logProgression(exerciseId: String, addedReps: Int, addedEccentric: Int, reducedRest: Int) {
        val currentLog = _progressionLogs.value[exerciseId] ?: ProgressionLog(exerciseId)
        _progressionLogs.value += (exerciseId to currentLog.copy(
            addedReps = currentLog.addedReps + addedReps,
            addedEccentricSeconds = currentLog.addedEccentricSeconds + addedEccentric,
            reducedRestSeconds = currentLog.reducedRestSeconds + reducedRest
        ))
    }
}

// --- Composables ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutAppScreen(viewModel: WorkoutViewModel = viewModel()) {
    val split by viewModel.workoutSplit.collectAsState()
    val currentDay by viewModel.currentDay.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hypertrophy Split", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(split) { day ->
                    val isSelected = day.id == currentDay.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectDay(day) },
                        label = { Text(day.name.take(3)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            AnimatedContent(
                targetState = currentDay,
                transitionSpec = {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                },
                label = "DayTransition"
            ) { day ->
                if (day.isActiveRecovery) {
                    ActiveRecoveryTracker(day)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = day.description,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        items(day.exercises, key = { it.id }) { exercise ->
                            ExerciseCard(exercise = exercise, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise, viewModel: WorkoutViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showSwapDialog by remember { mutableStateOf(false) }
    val progression by viewModel.progressionLogs.collectAsState()
    val log = progression[exercise.id]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = exercise.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = "${exercise.sets} x ${exercise.reps} | ${exercise.weight}", style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = { showSwapDialog = true }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Swap Exercise")
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                if (exercise.tempo != null) {
                    TempoVisualizer(exercise.tempo)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OverloadTracker(
                    log = log,
                    onAddRep = { viewModel.logProgression(exercise.id, 1, 0, 0) },
                    onAddEccentric = { viewModel.logProgression(exercise.id, 0, 1, 0) },
                    onReduceRest = { viewModel.logProgression(exercise.id, 0, 0, 15) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                val targetRest = maxOf(0, exercise.restSeconds - (log?.reducedRestSeconds ?: 0))
                FluidRestTimer(targetRest)
            }
        }
    }

    if (showSwapDialog) {
        val options = viewModel.getReplacementOptions(exercise.id)
        AlertDialog(
            onDismissRequest = { showSwapDialog = false },
            title = { Text("Swap Exercise (Shoulder-Safe)") },
            text = {
                LazyColumn {
                    items(options) { opt ->
                        ListItem(
                            headlineContent = { Text(opt.name) },
                            modifier = Modifier.clickable {
                                viewModel.swapExercise(exercise, opt)
                                showSwapDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwapDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun TempoVisualizer(tempo: Tempo) {
    Column {
        Text("Tempo Breakdown", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TempoPhase("Eccentric", tempo.eccentric)
            TempoPhase("Bottom", tempo.bottomPause)
            TempoPhase("Concentric", tempo.concentric)
            TempoPhase("Top", tempo.topPause)
        }
    }
}

@Composable
fun RowScope.TempoPhase(label: String, seconds: Int) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${seconds}s", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun OverloadTracker(
    log: ProgressionLog?,
    onAddRep: () -> Unit,
    onAddEccentric: () -> Unit,
    onReduceRest: () -> Unit
) {
    Column {
        Text("Progressive Overload", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OverloadButton("+1 Rep (${log?.addedReps ?: 0})", onAddRep)
            OverloadButton("+1s Ecc (${log?.addedEccentricSeconds ?: 0})", onAddEccentric)
            OverloadButton("-15s Rest (${log?.reducedRestSeconds ?: 0})", onReduceRest)
        }
    }
}

@Composable
fun OverloadButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun FluidRestTimer(totalSeconds: Int) {
    var isRunning by remember { mutableStateOf(false) }
    var remaining by remember { mutableStateOf(totalSeconds) }

    val progress by animateFloatAsState(
        targetValue = if (totalSeconds > 0) remaining.toFloat() / totalSeconds else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "restProgress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    @Suppress("UNUSED_VARIABLE")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "timerPulse"
    )

    LaunchedEffect(isRunning, remaining) {
        if (isRunning && remaining > 0) {
            delay(1000)
            remaining -= 1
        } else if (remaining == 0) {
            isRunning = false
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { isRunning = !isRunning }
        ) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(100.dp),
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.primary,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${remaining}s",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(if (isRunning) "Tap to Pause" else "Tap to Start Rest", style = MaterialTheme.typography.labelMedium)
        Button(onClick = { remaining = totalSeconds; isRunning = false }) {
            Text("Reset Timer")
        }
    }
}

@Composable
fun ActiveRecoveryTracker(day: WorkoutDay) {
    var checkWalk by remember { mutableStateOf(false) }
    var checkYoga by remember { mutableStateOf(false) }
    var checkFoamRoll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${day.name} - ${day.description}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.tertiary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recovery Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                RecoveryTask("Light Walking (10k steps)", checkWalk) { checkWalk = it }
                RecoveryTask("Yoga / Mobility Session", checkYoga) { checkYoga = it }
                RecoveryTask("Foam Rolling", checkFoamRoll) { checkFoamRoll = it }
            }
        }
    }
}

@Composable
fun RecoveryTask(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}
