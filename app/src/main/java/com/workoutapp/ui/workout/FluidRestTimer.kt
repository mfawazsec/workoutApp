package com.workoutapp.ui.workout

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun FluidRestTimer(totalSeconds: Int, accentColor: Color) {
    var isRunning by remember(totalSeconds) { mutableStateOf(false) }
    var remaining by remember(totalSeconds) { mutableStateOf(totalSeconds) }

    val progress by animateFloatAsState(
        targetValue = if (totalSeconds > 0) remaining.toFloat() / totalSeconds else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "restProgress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
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
        Text(
            "Rest Timer",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { isRunning = !isRunning }
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(100.dp),
                strokeWidth = 8.dp,
                color = accentColor,
                trackColor = accentColor.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${remaining}s",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isRunning) "Tap to pause" else "Tap to start rest",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { remaining = totalSeconds; isRunning = false },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.15f)),
        ) {
            Text("Reset", color = accentColor, fontWeight = FontWeight.SemiBold)
        }
    }
}
