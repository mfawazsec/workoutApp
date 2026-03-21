package com.workoutapp.ui.suggest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestScreen(
    onStartWorkout: (List<MuscleGroup>) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SuggestViewModel = viewModel()
) {
    val suggestedGroups by viewModel.suggestedGroups.collectAsState()
    val reason by viewModel.reason.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TODAY'S SUGGESTION",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(64.dp)
            )
            Text(
                "TODAY'S RECOMMENDATION",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
            Text(
                reason,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceMuted,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                suggestedGroups.forEach { group ->
                    val accentColor = when (group.id) {
                        "push" -> NeonOrange
                        "pull" -> NeonBlue
                        "legs" -> NeonGreen
                        "core_shoulders" -> NeonPurple
                        else -> NeonCyan
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                group.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onStartWorkout(suggestedGroups) },
                enabled = suggestedGroups.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "START THIS WORKOUT",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
