package com.workoutapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.home.BodySilhouette
import com.workoutapp.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartWorkout: (List<MuscleGroup>) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val selected by viewModel.selectedGroups.collectAsState()
    val suggested by viewModel.suggestedGroups.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Hypertrophy",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Body silhouette — interactive on home page
                BodySilhouette(
                    selectedGroups = selected,
                    onRegionTap = { group -> viewModel.toggleGroup(group) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select muscle groups",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose what to train today. 2–3 exercises per group.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Muscle group cards
            items(MuscleGroup.entries.size) { index ->
                val group = MuscleGroup.entries[index]
                MuscleGroupCard(
                    group = group,
                    isSelected = selected.contains(group),
                    onClick = { viewModel.toggleGroup(group) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Suggestion button
                    FilledTonalButton(
                        onClick = { viewModel.applySuggestion() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text("Today's Suggestion")
                    }

                    // Start workout button
                    Button(
                        onClick = { onStartWorkout(selected.toList()) },
                        enabled = selected.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = selected.firstOrNull()?.color
                                ?: MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Start Workout", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
