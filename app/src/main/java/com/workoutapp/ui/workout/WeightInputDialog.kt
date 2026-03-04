package com.workoutapp.ui.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workoutapp.data.util.RepMaxCalculator

@Composable
fun WeightInputDialog(
    currentWeightKg: Float,
    currentReps: Int,
    estimatedOneRepMaxKg: Float,
    accentColor: Color,
    onConfirm: (newWeightKg: Float, suggestedReps: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf(
        if (currentWeightKg == 0f) "" else currentWeightKg.let {
            if (it == it.toLong().toFloat()) it.toLong().toString() else "%.1f".format(it)
        }
    )}
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val parsedWeight = inputText.trim().toFloatOrNull()
    val suggestedReps = if (parsedWeight != null && parsedWeight > 0f && estimatedOneRepMaxKg > 0f) {
        RepMaxCalculator.suggestReps(estimatedOneRepMaxKg, parsedWeight)
    } else currentReps

    fun confirm() {
        val w = inputText.trim().toFloatOrNull()
        if (w == null || w < 0f) {
            errorMsg = "Enter a valid weight (0 for bodyweight)"
            return
        }
        onConfirm(w, suggestedReps)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Text(
                "Set Weight",
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it; errorMsg = null },
                    label = { Text("Weight (kg)") },
                    suffix = { Text("kg") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { confirm() }),
                    singleLine = true,
                    isError = errorMsg != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        focusedLabelColor = accentColor,
                        cursorColor = accentColor
                    )
                )
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        errorMsg!!,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (parsedWeight != null && parsedWeight > 0f && estimatedOneRepMaxKg > 0f) {
                    Spacer(modifier = Modifier.height(12.dp))
                    val zone = RepMaxCalculator.zone(suggestedReps)
                    Text(
                        "Suggested reps for hypertrophy: $suggestedReps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(zone.colorHex)
                    )
                    Text(
                        zone.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(zone.colorHex).copy(alpha = 0.8f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { confirm() }) {
                Text("Set", color = accentColor, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
