package com.workoutapp.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutapp.data.util.RepMaxCalculator
import com.workoutapp.data.util.RepZone
import com.workoutapp.ui.theme.EnduranceZoneColor
import com.workoutapp.ui.theme.HypertrophyColor
import com.workoutapp.ui.theme.OutOfRangeColor
import com.workoutapp.ui.theme.StrengthZoneColor

@Composable
fun RepsStepperControl(
    reps: Int,
    onRepsChange: (Int) -> Unit,
    accentColor: Color,
    isTimeBased: Boolean = false,
    modifier: Modifier = Modifier
) {
    val step = if (isTimeBased) 5 else 1
    val minValue = if (isTimeBased) 5 else 1

    val zone = remember(reps) { RepMaxCalculator.zone(reps) }
    val zoneColor = when (zone) {
        RepZone.STRENGTH     -> StrengthZoneColor
        RepZone.HYPERTROPHY  -> HypertrophyColor
        RepZone.ENDURANCE    -> EnduranceZoneColor
        RepZone.OUT_OF_RANGE -> OutOfRangeColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            if (isTimeBased) "sec" else "Reps",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Small tap target — 28 dp circle, no IconButton overhead
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.13f))
                    .clickable(enabled = reps > minValue) { onRepsChange(reps - step) }
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = if (isTimeBased) "Decrease seconds" else "Decrease reps",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = "$reps",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTimeBased) accentColor else zoneColor
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.13f))
                    .clickable { onRepsChange(reps + step) }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = if (isTimeBased) "Increase seconds" else "Increase reps",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun SetsStepperControl(
    sets: Int,
    onSetsChange: (Int) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            "Sets",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.13f))
                    .clickable(enabled = sets > 1) { onSetsChange(sets - 1) }
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease sets",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = "$sets",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.13f))
                    .clickable(enabled = sets < 10) { onSetsChange(sets + 1) }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase sets",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
