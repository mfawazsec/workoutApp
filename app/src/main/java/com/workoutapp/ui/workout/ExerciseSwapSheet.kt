package com.workoutapp.ui.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workoutapp.data.model.Exercise
import com.workoutapp.data.model.MuscleGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSwapSheet(
    currentExercise: Exercise,
    alternatives: List<Exercise>,
    onSelect: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    val group = MuscleGroup.fromId(currentExercise.muscleGroupId)
    val accentColor = group?.color ?: MaterialTheme.colorScheme.primary
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                "Swap Exercise",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Alternatives for: ${currentExercise.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (alternatives.isEmpty()) {
            Text(
                "No other exercises available for this group.",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(alternatives) { exercise ->
                    SwapExerciseRow(exercise = exercise, accentColor = accentColor) {
                        onSelect(exercise)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SwapExerciseRow(
    exercise: Exercise,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${exercise.sets} × ${exercise.reps}  ·  ${exercise.equipment}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = accentColor.copy(alpha = 0.15f)
            ) {
                Text(
                    "Select",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
}
