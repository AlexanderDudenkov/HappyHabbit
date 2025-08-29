package com.dudencov.happyhabit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogNode
import com.dudencov.happyhabit.presentation.detail.DetailScreenNode
import com.dudencov.happyhabit.presentation.habitdialog.HabitDialogNode
import com.dudencov.happyhabit.feature.home.HomeScreenNode
import com.dudencov.happyhabit.core.navigation.Routes
import com.dudencov.happyhabit.presentation.notification.NotificationScreenNode
import com.dudencov.happyhabit.presentation.settings.SettingsScreenNode
import com.dudencov.happyhabit.presentation.utils.animatedComposable
import com.dudencov.happyhabit.presentation.weekly.WeeklyProgressScreenNode

@Composable
fun AppNavHost(navController: NavHostController) {
    val navigator = remember { NavigatorImpl(navController) }

    NavHost(navController = navController, startDestination = Routes.Home.ROUTE_PATTERN) {
        animatedComposable(
            route = Routes.Home.ROUTE_PATTERN,
            arguments = listOf(navArgument(Routes.Home.HABIT_ID_ARG) {
                type = NavType.StringType
                nullable = true
            })
        ) {
            HomeScreenNode(navigator, hiltViewModel())
        }

        animatedComposable(
            route = Routes.Detail.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.Detail.HABIT_ID_ARG) {
                    type = NavType.StringType
                },
                navArgument(Routes.Detail.HABIT_NAME_ARG) {
                    type = NavType.StringType
                })
        ) {
            DetailScreenNode(navController, hiltViewModel())
        }

        animatedComposable(Routes.WeeklyProgress.ROUTE_PATTERN) {
            WeeklyProgressScreenNode(navController, hiltViewModel())
        }

        animatedComposable(Routes.Notification.ROUTE_PATTERN) {
            NotificationScreenNode(navController, hiltViewModel())
        }

        animatedComposable(Routes.Settings.ROUTE_PATTERN) {
            SettingsScreenNode(navController, hiltViewModel())
        }

        dialog(
            route = Routes.HabitDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.HabitDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                }
            )) {
            HabitDialogNode(navController, hiltViewModel())
        }

        dialog(
            route = Routes.DeleteConfirmationDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.DeleteConfirmationDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                }
            )) {
            DeleteConfirmationDialogNode(navController, hiltViewModel())
        }
    }
}

