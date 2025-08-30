package com.dudencov.happyhabit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.dudencov.happyhabit.core.navigation.Routes
import com.dudencov.happyhabit.feature.deleteconfirmationdialog.DeleteConfirmationDialogNode
import com.dudencov.happyhabit.feature.detail.DetailScreenNode
import com.dudencov.happyhabit.feature.habitdialog.HabitDialogNode
import com.dudencov.happyhabit.feature.home.HomeScreenNode
import com.dudencov.happyhabit.feature.notification.NotificationScreenNode
import com.dudencov.happyhabit.feature.settings.SettingsScreenNode
import com.dudencov.happyhabit.feature.weekly.WeeklyProgressScreenNode
import com.dudencov.happyhabit.utils.animatedComposable

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
            DetailScreenNode(navigator, hiltViewModel())
        }

        animatedComposable(Routes.WeeklyProgress.ROUTE_PATTERN) {
            WeeklyProgressScreenNode(navigator, hiltViewModel())
        }

        animatedComposable(Routes.Notification.ROUTE_PATTERN) {
            NotificationScreenNode(navigator, hiltViewModel())
        }

        animatedComposable(Routes.Settings.ROUTE_PATTERN) {
            SettingsScreenNode(navigator, hiltViewModel())
        }

        dialog(
            route = Routes.HabitDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.HabitDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                }
            )) {
            HabitDialogNode(navigator, hiltViewModel())
        }

        dialog(
            route = Routes.DeleteConfirmationDialog.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.DeleteConfirmationDialog.HABIT_ID_ARG) {
                    type = NavType.StringType
                }
            )) {
            DeleteConfirmationDialogNode(navigator, hiltViewModel())
        }
    }
}

