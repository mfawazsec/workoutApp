package com.workoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.workoutapp.ui.navigation.WorkoutNavGraph
import com.workoutapp.ui.theme.WorkoutAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutAppTheme {
                val navController = rememberNavController()
                WorkoutNavGraph(navController = navController)
            }
        }
    }
}
