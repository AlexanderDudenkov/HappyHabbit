package com.dudencov.happyhabit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogNode
import com.dudencov.happyhabit.presentation.detail.DetailScreenNode
import com.dudencov.happyhabit.presentation.habitdialog.HabitDialogNode
import com.dudencov.happyhabit.presentation.home.HomeScreenNode
import com.dudencov.happyhabit.presentation.notification.NotificationScreenNode
import com.dudencov.happyhabit.presentation.settings.SettingsScreenNode
import com.dudencov.happyhabit.presentation.utils.animatedComposable
import com.dudencov.happyhabit.presentation.weekly.WeeklyProgressScreenNode

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Home.ROUTE_PATTERN) {
        animatedComposable(
            route = Routes.Home.ROUTE_PATTERN,
            arguments = listOf(navArgument(Routes.Home.HABIT_ID_ARG) {
                type = NavType.StringType
                nullable = true
            })
        ) {
            HomeScreenNode(navController)
        }

        animatedComposable(
            route = Routes.Detail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Routes.Detail.HABIT_ID_ARG) {
                type = NavType.StringType
            })
        ) {
            DetailScreenNode(navController)
        }

        animatedComposable(Routes.WeeklyProgress.ROUTE_PATTERN) {
            WeeklyProgressScreenNode(navController)
        }

        animatedComposable(Routes.Notification.ROUTE_PATTERN) {
            NotificationScreenNode(navController)
        }

        animatedComposable(Routes.Settings.ROUTE_PATTERN) {
            SettingsScreenNode(navController)
        }

        dialog(
            route = Routes.HabitDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.HabitDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                }
            )) {
            HabitDialogNode(navController)
        }

        dialog(
            route = Routes.DeleteConfirmationDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.DeleteConfirmationDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                }
            )) {
            DeleteConfirmationDialogNode(navController)
        }
    }
}

