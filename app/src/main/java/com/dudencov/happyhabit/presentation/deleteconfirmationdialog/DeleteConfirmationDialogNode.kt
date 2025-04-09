package com.dudencov.happyhabit.presentation.deleteconfirmationdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun DeleteConfirmationDialogNode(navController: NavHostController) {
    val viewModel: DeleteConfirmationDialogViewModel = hiltViewModel()

    HandleArgs(navController, viewModel)
    HandleSideEffects(viewModel, navController)

    DeleteConfirmationDialog(
        onConfirm = { viewModel.onIntent(DeleteConfirmationDialogIntent.OnConfirm) },
        onDismiss = { viewModel.onIntent(DeleteConfirmationDialogIntent.OnDismiss) }
    )
}

@Composable
private fun HandleArgs(
    navController: NavHostController,
    viewModel: DeleteConfirmationDialogViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val habitId = navBackStackEntry?.arguments?.getString(Routes.DeleteConfirmationDialog.HABIT_ID_ARG)?.toInt() ?: return

    LaunchedEffect(habitId) {
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnSetHabitId(habitId))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: DeleteConfirmationDialogViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                DeleteConfirmationDialogSideEffect.OnDismiss -> {
                    navController.popBackStack()
                }
            }
        }
    }
} 