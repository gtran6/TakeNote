package com.example.takenotecomposeapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.takenotecomposeapp.TakeNoteDestinationsArgs.USER_MESSAGE_ARG
import com.example.takenotecomposeapp.ui.taskdetail.TaskDetailScreen
import com.example.takenotecomposeapp.ui.tasks.TasksScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TakeNoteDestinations.TASKS_ROUTE,
    navActions: TakeNoteNavigationActions = remember(navController) {
        TakeNoteNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            TakeNoteDestinations.TASKS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )) { entry ->
            TasksScreen(
                userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                onTaskClick = { task -> navActions.navigateToTaskDetail(task.id) },
                onUserMessageDisplayed = { entry.arguments?.putInt(USER_MESSAGE_ARG, 0) },
            )
        }

        composable(TakeNoteDestinations.TASK_DETAIL_ROUTE) {
            TaskDetailScreen(
                onEditTask = { taskId ->
                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
                },
                onBackPressed = { navController.popBackStack() },
                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK) }
            )
        }
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3