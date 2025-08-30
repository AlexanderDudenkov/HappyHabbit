package com.dudencov.happyhabit.feature.habitdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dudencov.happyhabit.core.navigation.Navigator
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun HabitDialogNode(navigator: Navigator, viewModel: HabitDialogViewModel) {
    val state by viewModel.state.collectAsState()

    HandleArgs(navigator, viewModel)
    HandleSideEffects(viewModel, navigator)

    HabitDialog(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun HandleArgs(
    navigator: Navigator,
    viewModel: HabitDialogViewModel
) {
    val habitId = navigator.getIntArgument(Routes.HabitDialog.HABIT_ID_ARG) ?: return

    LaunchedEffect(habitId) {
        viewModel.onIntent(HabitDialogIntent.OnChangeTitle(HabitDialogTitle.EDIT))
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: HabitDialogViewModel,
    navigator: Navigator
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                HabitDialogSideEffect.OnDismiss -> {
                    navigator.popBackStack()
                }
            }
        }
    }
}
