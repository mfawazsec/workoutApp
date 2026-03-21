package com.workoutapp.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.entity.ExerciseSnapshotEntity
import com.workoutapp.data.entity.WorkoutSessionEntity
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

private val DATE_FMT = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.getDefault())
private val TIME_FMT = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val sessions by viewModel.completedSessions.collectAsState()
    val details by viewModel.sessionDetails.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "HISTORY",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp,
                            color = NeonCyan
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = OnSurfaceMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No completed workouts yet",
                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceMuted)
                    )
                    Text(
                        "Complete a session to see it here",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceMuted.copy(alpha = 0.6f))
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sessions, key = { it.sessionId }) { session ->
                    val snapshots = details[session.sessionId]
                    SessionCard(
                        session = session,
                        snapshots = snapshots,
                        onExpand = { viewModel.loadSnapshots(session.sessionId) }
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun SessionCard(
    session: WorkoutSessionEntity,
    snapshots: List<ExerciseSnapshotEntity>?,
    onExpand: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val groupIds = session.muscleGroupIds.split(",").filter { it.isNotBlank() }
    val groups = groupIds.mapNotNull { MuscleGroup.fromId(it) }
    val primaryColor = groups.firstOrNull()?.color ?: NeonCyan

    val date = LocalDate.ofEpochDay(session.dateEpochDay)
    val dateStr = date.format(DATE_FMT)

    val durationStr = if (session.startedAtMs > 0 && session.completedAt != null) {
        val mins = TimeUnit.MILLISECONDS.toMinutes(session.completedAt - session.startedAtMs)
        "${mins} min"
    } else null

    val completedTimeStr = session.completedAt?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).format(TIME_FMT)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                        if (expanded) onExpand()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        dateStr,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = OnSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        groups.forEach { group ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = group.color.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    group.displayName,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = group.color,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                    if (durationStr != null || completedTimeStr != null) {
                        Spacer(Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (durationStr != null) {
                                Text(
                                    durationStr,
                                    style = MaterialTheme.typography.labelSmall.copy(color = primaryColor)
                                )
                            }
                            if (completedTimeStr != null) {
                                Text(
                                    "finished $completedTimeStr",
                                    style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceMuted)
                                )
                            }
                        }
                    }
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expanded: exercise list
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = primaryColor.copy(alpha = 0.12f))
                    Spacer(Modifier.height(12.dp))

                    if (snapshots == null) {
                        Text(
                            "Loading…",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceMuted),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else if (snapshots.isEmpty()) {
                        Text(
                            "No exercise data recorded",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceMuted),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        // Build global timeline of all set timestamps for superset rest calculation
                        val globalTimeline = snapshots.flatMap { snap ->
                            snap.setCompletionTimestamps
                                .split(",")
                                .filter { it.isNotBlank() }
                                .mapNotNull { it.toLongOrNull() }
                        }.sorted()

                        snapshots.forEach { snap ->
                            ExerciseHistoryRow(snap, primaryColor, globalTimeline)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseHistoryRow(
    snap: ExerciseSnapshotEntity,
    accentColor: Color,
    globalTimeline: List<Long> = emptyList()
) {
    val doneTimeStr = snap.completedAtMs?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).format(TIME_FMT)
    }

    val weightLabel = when {
        snap.weightKg == 0f -> "BW"
        snap.weightKg == snap.weightKg.toLong().toFloat() -> "${snap.weightKg.toLong()} kg"
        else -> "${"%.1f".format(snap.weightKg)} kg"
    }

    val setTimestamps = snap.setCompletionTimestamps
        .split(",")
        .filter { it.isNotBlank() }
        .mapNotNull { it.toLongOrNull() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (snap.completedAtMs != null) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Spacer(Modifier.size(14.dp))
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        snap.exerciseName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurface,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        if (snap.isTimeBased)
                            "${snap.sets} sets × ${snap.reps}s"
                        else
                            "${snap.sets} sets × $weightLabel × ${snap.reps} reps",
                        style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceMuted)
                    )
                }
            }
            if (doneTimeStr != null) {
                Text(
                    doneTimeStr,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        // Rest times between sets (uses global timeline for accurate superset rest)
        if (setTimestamps.size >= 2) {
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 22.dp)
            ) {
                setTimestamps.forEachIndexed { idx, ts ->
                    if (idx > 0) {
                        // Find the previous activity in the global timeline (from any exercise)
                        val globalIdx = globalTimeline.indexOfLast { it < ts }
                        val prevTs = if (globalIdx >= 0) globalTimeline[globalIdx] else setTimestamps[idx - 1]
                        val restMs = ts - prevTs
                        val restMin = TimeUnit.MILLISECONDS.toMinutes(restMs)
                        val restSec = TimeUnit.MILLISECONDS.toSeconds(restMs) % 60
                        val restStr = if (restMin > 0) "${restMin}m ${restSec}s" else "${restSec}s"
                        Text(
                            "· $restStr ·",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceMuted.copy(alpha = 0.5f),
                                fontSize = 9.sp
                            )
                        )
                    }
                    Text(
                        "S${idx + 1}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = accentColor.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    )
                }
            }
        }
    }
}
