package com.workoutapp.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit,
    viewModel: CalendarViewModel = viewModel()
) {
    val recentTracking by viewModel.recentTracking.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()

    // Build a map of date -> DailyTrackingEntity
    val trackingMap = recentTracking.associateBy { it.date }
    val workoutDates = recentSessions.map {
        LocalDate.ofEpochDay(it.dateEpochDay).toString()
    }.toSet()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CALENDAR",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HeatmapSection(
                title = "WORKOUT",
                accentColor = NeonOrange,
                getCellColor = { date ->
                    if (workoutDates.contains(date)) NeonOrange else Color.Transparent
                }
            )
            HeatmapSection(
                title = "WATER",
                accentColor = NeonCyan,
                getCellColor = { date ->
                    val glasses = trackingMap[date]?.waterGlasses ?: 0
                    val intensity = (glasses.toFloat() / 8f).coerceIn(0f, 1f)
                    if (intensity > 0f) NeonCyan.copy(alpha = 0.2f + intensity * 0.8f) else Color.Transparent
                }
            )
            HeatmapSection(
                title = "CREATINE",
                accentColor = NeonPurple,
                getCellColor = { date ->
                    val taken = (trackingMap[date]?.creatineServings ?: 0) > 0
                    if (taken) NeonPurple else Color.Transparent
                }
            )
        }
    }
}

@Composable
private fun HeatmapSection(
    title: String,
    accentColor: Color,
    getCellColor: (String) -> Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = accentColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        )
        // Generate last 16 weeks of dates
        val today = LocalDate.now()
        val weeks = 16
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            // Day labels column
            Column(
                modifier = Modifier.width(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                dayLabels.forEach { day ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .padding(bottom = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceMuted,
                                fontSize = 7.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }

            // Weeks columns
            for (weekOffset in (weeks - 1) downTo 0) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (dayOfWeek in 1..7) {
                        val date = today
                            .minusWeeks(weekOffset.toLong())
                            .with(DayOfWeek.of(dayOfWeek))
                        val dateStr = date.toString()
                        val cellColor = getCellColor(dateStr)
                        val isFuture = date.isAfter(today)

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (isFuture) Color.Transparent
                                    else if (cellColor == Color.Transparent) CardSurfaceElevated
                                    else cellColor
                                )
                        )
                    }
                }
            }
        }
    }
}
