package com.workoutapp.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class OverloadLog(
    val addedReps: Int = 0,
    val addedEccentricSeconds: Int = 0,
    val reducedRestSeconds: Int = 0
)

@Composable
fun OverloadTracker(
    log: OverloadLog?,
    accentColor: Color,
    onAddRep: () -> Unit,
    onAddEccentric: () -> Unit,
    onReduceRest: () -> Unit
) {
    Column {
        Text(
            "Progressive Overload",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OverloadChip("+1 Rep (${log?.addedReps ?: 0})", accentColor, onAddRep, Modifier.weight(1f))
            OverloadChip("+1s Ecc (${log?.addedEccentricSeconds ?: 0})", accentColor, onAddEccentric, Modifier.weight(1f))
            OverloadChip("-15s Rest (${log?.reducedRestSeconds ?: 0})", accentColor, onReduceRest, Modifier.weight(1f))
        }
    }
}

@Composable
private fun OverloadChip(
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = accentColor
        )
    }
}
