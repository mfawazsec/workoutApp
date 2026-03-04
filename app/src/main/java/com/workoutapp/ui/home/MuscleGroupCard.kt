package com.workoutapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.CardSurface

@Composable
fun MuscleGroupCard(
    group: MuscleGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) group.color.copy(alpha = 0.15f) else CardSurface,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) group.color else Color(0xFF2C2C2C),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardBorder"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) group.color else MaterialTheme.colorScheme.onSurface,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "textColor"
    )
    // Subtle press scale — gives satisfying tactile feedback
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.015f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "cardScale"
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = borderColor.copy(alpha = if (isSelected) 0.85f else 0.25f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 0.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group colour dot
            Surface(
                modifier = Modifier.size(10.dp),
                shape = CircleShape,
                color = group.color.copy(alpha = if (isSelected) 1f else 0.5f)
            ) {}
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = group.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.65f)
                )
            }

            // Animated checkmark appears when selected
            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = group.color.copy(alpha = 0.15f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = group.color,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}
