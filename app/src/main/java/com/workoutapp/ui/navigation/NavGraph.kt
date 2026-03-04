package com.workoutapp.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.workoutapp.ui.calendar.CalendarScreen
import com.workoutapp.ui.home.HomeScreen
import com.workoutapp.ui.suggest.SuggestScreen
import com.workoutapp.ui.theme.*
import com.workoutapp.ui.workout.WorkoutScreen
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "home"
    private const val WORKOUT_BASE = "workout"
    const val WORKOUT = "$WORKOUT_BASE/{muscleGroupIds}"
    const val CALENDAR = "calendar"
    const val SUGGEST = "suggest"

    fun workoutRoute(muscleGroupIds: List<String>): String =
        "$WORKOUT_BASE/${muscleGroupIds.joinToString(",")}"
}

private const val ANIM_MS = 350
private val easing = FastOutSlowInEasing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutNavGraph(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AtlasDrawerContent(
                onNavigateToWorkout = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = false } }
                },
                onNavigateToHistoryWorkout = {
                    scope.launch { drawerState.close() }
                    // Future: navigate to workout history screen
                },
                onNavigateToHistoryWater = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.CALENDAR)
                },
                onNavigateToHistoryCreatine = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.CALENDAR)
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        },
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(ANIM_MS, easing = easing)) + fadeIn(tween(ANIM_MS))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(ANIM_MS, easing = easing)) + fadeOut(tween(ANIM_MS))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(ANIM_MS, easing = easing)) + fadeIn(tween(ANIM_MS))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(ANIM_MS, easing = easing)) + fadeOut(tween(ANIM_MS))
            }
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onStartWorkout = { selectedGroups ->
                        navController.navigate(Routes.workoutRoute(selectedGroups.map { it.id }))
                    },
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onOpenCalendar = { navController.navigate(Routes.CALENDAR) },
                    onOpenSuggest = { navController.navigate(Routes.SUGGEST) }
                )
            }
            composable(
                route = Routes.WORKOUT,
                arguments = listOf(navArgument("muscleGroupIds") { type = NavType.StringType })
            ) { backStackEntry ->
                val raw = backStackEntry.arguments?.getString("muscleGroupIds") ?: ""
                val groupIds = raw.split(",").filter { it.isNotBlank() }
                WorkoutScreen(
                    muscleGroupIds = groupIds,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.CALENDAR) {
                CalendarScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Routes.SUGGEST) {
                SuggestScreen(
                    onStartWorkout = { groups ->
                        navController.navigate(Routes.workoutRoute(groups.map { it.id }))
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun AtlasDrawerContent(
    onNavigateToWorkout: () -> Unit,
    onNavigateToHistoryWorkout: () -> Unit,
    onNavigateToHistoryWater: () -> Unit,
    onNavigateToHistoryCreatine: () -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        drawerContainerColor = CardSurface
    ) {
        Spacer(Modifier.height(48.dp))
        Text(
            "ATLAS",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = NeonCyan.copy(alpha = 0.2f))
        Spacer(Modifier.height(8.dp))

        DrawerItem(icon = Icons.Default.FitnessCenter, label = "WORKOUT", onClick = onNavigateToWorkout)
        DrawerItem(icon = Icons.Default.History, label = "HISTORY — WORKOUT", onClick = onNavigateToHistoryWorkout)
        DrawerItem(icon = Icons.Default.WaterDrop, label = "HISTORY — WATER", onClick = onNavigateToHistoryWater)
        DrawerItem(icon = Icons.Default.Science, label = "HISTORY — CREATINE", onClick = onNavigateToHistoryCreatine)
    }
}

@Composable
private fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, tint = NeonCyan) },
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurface,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent
        )
    )
}
