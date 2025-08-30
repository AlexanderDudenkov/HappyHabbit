package com.dudencov.happyhabit.feature.deleteconfirmationdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dudencov.happyhabit.core.navigation.Navigator
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun DeleteConfirmationDialogNode(
    navigator: Navigator,
    viewModel: DeleteConfirmationDialogViewModel
) {
    HandleArgs(navigator, viewModel)
    HandleSideEffects(viewModel, navigator)

    DeleteConfirmationDialog(
        onConfirm = { viewModel.onIntent(DeleteConfirmationDialogIntent.OnConfirm) },
        onDismiss = { viewModel.onIntent(DeleteConfirmationDialogIntent.OnDismiss) }
    )
}

@Composable
private fun HandleArgs(
    navigator: Navigator,
    viewModel: DeleteConfirmationDialogViewModel
) {
    val habitId = navigator.getIntArgument(Routes.DeleteConfirmationDialog.HABIT_ID_ARG) ?: return

    LaunchedEffect(habitId) {
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnSetHabitId(habitId))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: DeleteConfirmationDialogViewModel,
    navigator: Navigator
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                DeleteConfirmationDialogSideEffect.OnDismiss -> {
                    navigator.popBackStack()
                }
            }
        }
    }
} 