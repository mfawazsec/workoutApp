package com.workoutapp.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
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
import java.time.format.TextStyle
import java.util.Locale

private const val WEEKS = 22          // ~5 months visible
private const val CELL_DP = 12        // cell size
private const val GAP_DP = 2          // gap between cells
private val DAY_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit,
    viewModel: CalendarViewModel = viewModel()
) {
    val recentTracking by viewModel.recentTracking.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()

    val trackingMap = recentTracking.associateBy { it.date }
    val workoutDates = recentSessions
        .filter { it.completedAt != null }
        .map { LocalDate.ofEpochDay(it.dateEpochDay).toString() }
        .toSet()

    val workoutStreak = calcStreak { workoutDates.contains(it) }
    val waterStreak   = calcStreak { (trackingMap[it]?.waterGlasses ?: 0) > 0 }
    val creatineStreak = calcStreak { (trackingMap[it]?.creatineServings ?: 0) > 0 }
    val multivitaminStreak = calcStreak { (trackingMap[it]?.multivitaminTaken ?: 0) > 0 }

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeatmapCard(
                title = "WORKOUT",
                accentColor = NeonOrange,
                streak = workoutStreak,
                getCellColor = { date ->
                    if (workoutDates.contains(date)) NeonOrange else Color.Transparent
                }
            )
            HeatmapCard(
                title = "WATER",
                accentColor = NeonCyan,
                streak = waterStreak,
                getCellColor = { date ->
                    val glasses = trackingMap[date]?.waterGlasses ?: 0
                    val intensity = (glasses.toFloat() / 8f).coerceIn(0f, 1f)
                    if (intensity > 0f) NeonCyan.copy(alpha = 0.2f + intensity * 0.8f)
                    else Color.Transparent
                }
            )
            HeatmapCard(
                title = "CREATINE",
                accentColor = NeonPurple,
                streak = creatineStreak,
                getCellColor = { date ->
                    val taken = (trackingMap[date]?.creatineServings ?: 0) > 0
                    if (taken) NeonPurple else Color.Transparent
                }
            )
            HeatmapCard(
                title = "MULTIVITAMIN",
                accentColor = NeonGreen,
                streak = multivitaminStreak,
                getCellColor = { date ->
                    val taken = (trackingMap[date]?.multivitaminTaken ?: 0) > 0
                    if (taken) NeonGreen else Color.Transparent
                }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HeatmapCard(
    title: String,
    accentColor: Color,
    streak: Int,
    getCellColor: (String) -> Color
) {
    val today = LocalDate.now()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Section title
            Text(
                title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )

            // Grid with fixed day-labels + scrollable month/cells
            val hScrollState = rememberScrollState()
            LaunchedEffect(Unit) { hScrollState.scrollTo(hScrollState.maxValue) }

            Row(verticalAlignment = Alignment.Top) {
                // Fixed day-label column (not scrolled)
                Column(
                    modifier = Modifier.padding(top = (CELL_DP + GAP_DP).dp), // align below month row
                    verticalArrangement = Arrangement.spacedBy(GAP_DP.dp)
                ) {
                    DAY_LABELS.forEach { label ->
                        Box(
                            modifier = Modifier
                                .height(CELL_DP.dp)
                                .width(28.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceMuted,
                                    fontSize = 7.sp
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.width(4.dp))

                // Scrollable: month labels row + cell grid
                Row(
                    modifier = Modifier.horizontalScroll(hScrollState),
                    horizontalArrangement = Arrangement.spacedBy(GAP_DP.dp)
                ) {
                    for (weekOffset in (WEEKS - 1) downTo 0) {
                        val monday = today.minusWeeks(weekOffset.toLong())
                            .with(DayOfWeek.MONDAY)
                        val prevMonday = if (weekOffset < WEEKS - 1)
                            today.minusWeeks((weekOffset + 1).toLong()).with(DayOfWeek.MONDAY)
                        else null

                        val showMonth = prevMonday == null || monday.month != prevMonday.month
                        val monthLabel = buildMonthLabel(monday, prevMonday)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(GAP_DP.dp)
                        ) {
                            // Month label row
                            Box(
                                modifier = Modifier
                                    .width(CELL_DP.dp)
                                    .height((CELL_DP + GAP_DP).dp),
                                contentAlignment = Alignment.TopStart
                            ) {
                                if (showMonth) {
                                    Text(
                                        monthLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = OnSurfaceMuted,
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }

                            // 7 day cells
                            for (dayOfWeek in 1..7) {
                                val date = today
                                    .minusWeeks(weekOffset.toLong())
                                    .with(DayOfWeek.of(dayOfWeek))
                                val dateStr = date.toString()
                                val isFuture = date.isAfter(today)
                                val cellColor = if (isFuture) Color.Transparent
                                               else getCellColor(dateStr)

                                Box(
                                    modifier = Modifier
                                        .size(CELL_DP.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(
                                            if (cellColor == Color.Transparent) CardSurfaceElevated
                                            else cellColor
                                        )
                                )
                            }
                        }
                    }
                }
            }

            // Bottom bar: streak
            HorizontalDivider(color = accentColor.copy(alpha = 0.1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "STREAK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceMuted,
                        letterSpacing = 1.sp
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = if (streak > 0) Color(0xFFFF6B35) else OnSurfaceMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        if (streak > 0) "$streak day${if (streak == 1) "" else "s"}" else "—",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (streak > 0) Color(0xFFFF6B35) else OnSurfaceMuted,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

/** Show abbreviated month; add short year when year changes from previous week. */
private fun buildMonthLabel(monday: LocalDate, prevMonday: LocalDate?): String {
    val monthShort = monday.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    return if (prevMonday != null && monday.year != prevMonday.year) {
        "${monthShort} '${monday.year.toString().takeLast(2)}"
    } else {
        monthShort
    }
}

/** Count consecutive days going back where [isActive] returns true. */
private fun calcStreak(isActive: (String) -> Boolean): Int {
    var count = 0
    var date = LocalDate.now()
    // If today not active, try starting from yesterday
    if (!isActive(date.toString())) date = date.minusDays(1)
    while (isActive(date.toString())) {
        count++
        date = date.minusDays(1)
    }
    return count
}
