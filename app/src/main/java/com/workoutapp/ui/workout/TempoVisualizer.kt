package com.workoutapp.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workoutapp.data.model.Tempo

@Composable
fun TempoVisualizer(tempo: Tempo, accentColor: androidx.compose.ui.graphics.Color) {
    Column {
        Text(
            "Tempo  ${tempo}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TempoPhase("Eccentric", tempo.eccentric, accentColor)
            TempoPhase("Pause", tempo.bottomPause, accentColor)
            TempoPhase("Concentric", tempo.concentric, accentColor)
            TempoPhase("Top", tempo.topPause, accentColor)
        }
    }
}

@Composable
private fun RowScope.TempoPhase(
    label: String,
    seconds: Int,
    accentColor: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(accentColor.copy(alpha = 0.12f))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${seconds}s",
            fontWeight = FontWeight.Bold,
            color = accentColor,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
