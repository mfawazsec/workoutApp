package com.workoutapp.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.workoutapp.data.model.MuscleGroup
import com.workoutapp.ui.home.BodySilhouette
import com.workoutapp.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    muscleGroupIds: List<String>,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val viewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModel.Factory(application, muscleGroupIds)
    )

    val exerciseStates by viewModel.exercises.collectAsState()
    val groups = viewModel.muscleGroups
    val primaryColor = groups.firstOrNull()?.color ?: MaterialTheme.colorScheme.primary

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Coloured group chips in title
                        groups.forEach { group ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = group.color.copy(alpha = 0.2f),
                                modifier = Modifier.padding(end = 6.dp)
                            ) {
                                Text(
                                    group.displayName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = group.color,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.completeSession()
                        onNavigateBack()
                    }) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Finish",
                            tint = primaryColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Done", color = primaryColor, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Read-only body silhouette showing today's targets
                val selectedGroupSet = groups.toSet()
                BodySilhouette(
                    selectedGroups = selectedGroupSet,
                    onRegionTap = null,  // read-only on workout screen
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (exerciseStates.isEmpty()) {
                item {
                    Text(
                        "Loading exercises…",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(exerciseStates, key = { it.exercise.id }) { state ->
                    ExerciseCard(
                        exercise = state.exercise,
                        weightKg = state.weightKg,
                        reps = state.reps,
                        estimatedOneRepMaxKg = state.estimatedOneRepMaxKg,
                        overloadLog = state.overloadLog,
                        alternatives = viewModel.getAlternatives(state.exercise),
                        onWeightChange = { newWeight, suggestedReps ->
                            viewModel.updateWeight(state.exercise.id, newWeight, suggestedReps)
                        },
                        onRepsChange = { newReps ->
                            viewModel.updateReps(state.exercise.id, newReps)
                        },
                        onSwap = { replacement ->
                            viewModel.swapExercise(state.exercise, replacement)
                        },
                        onAddRep = {
                            viewModel.logProgression(state.exercise.id, 1, 0, 0)
                        },
                        onAddEccentric = {
                            viewModel.logProgression(state.exercise.id, 0, 1, 0)
                        },
                        onReduceRest = {
                            viewModel.logProgression(state.exercise.id, 0, 0, 15)
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
