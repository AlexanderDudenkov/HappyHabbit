package com.dudencov.happyhabit.presentation.habitdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun HabitDialogNode(navController: NavHostController) {
    val viewModel: HabitDialogViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    HandleArgs(navController, viewModel)
    HandleSideEffects(viewModel, navController)

    HabitDialog(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun HandleArgs(
    navController: NavHostController,
    viewModel: HabitDialogViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val habitId = navBackStackEntry?.arguments?.getString(Routes.HabitDialog.HABIT_ID_ARG) ?: return

    LaunchedEffect(habitId) {
        viewModel.onIntent(HabitDialogIntent.OnChangeTitle(HabitDialogTitle.EDIT))
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: HabitDialogViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                HabitDialogSideEffect.OnDismiss -> {
                    navController.popBackStack()
                }
            }
        }
    }
}
