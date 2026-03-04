package com.workoutapp.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.workoutapp.ui.home.HomeScreen
import com.workoutapp.ui.workout.WorkoutScreen

object Routes {
    const val HOME = "home"
    private const val WORKOUT_BASE = "workout"
    const val WORKOUT = "$WORKOUT_BASE/{muscleGroupIds}"

    fun workoutRoute(muscleGroupIds: List<String>): String =
        "$WORKOUT_BASE/${muscleGroupIds.joinToString(",")}"
}

private const val ANIM_MS = 350

private val easing = FastOutSlowInEasing

@Composable
fun WorkoutNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        // Default transitions for the whole graph
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(ANIM_MS, easing = easing)
            ) + fadeIn(tween(ANIM_MS))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(ANIM_MS, easing = easing)
            ) + fadeOut(tween(ANIM_MS))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(ANIM_MS, easing = easing)
            ) + fadeIn(tween(ANIM_MS))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(ANIM_MS, easing = easing)
            ) + fadeOut(tween(ANIM_MS))
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartWorkout = { selectedGroups ->
                    navController.navigate(
                        Routes.workoutRoute(selectedGroups.map { it.id })
                    )
                }
            )
        }

        composable(
            route = Routes.WORKOUT,
            arguments = listOf(
                navArgument("muscleGroupIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("muscleGroupIds") ?: ""
            val groupIds = raw.split(",").filter { it.isNotBlank() }
            WorkoutScreen(
                muscleGroupIds = groupIds,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
