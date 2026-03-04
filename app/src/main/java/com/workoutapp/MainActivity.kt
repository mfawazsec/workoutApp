package com.workoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.workoutapp.ui.navigation.WorkoutNavGraph
import com.workoutapp.ui.theme.AtlasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AtlasTheme {
                val navController = rememberNavController()
                WorkoutNavGraph(navController = navController)
            }
        }
    }
}
