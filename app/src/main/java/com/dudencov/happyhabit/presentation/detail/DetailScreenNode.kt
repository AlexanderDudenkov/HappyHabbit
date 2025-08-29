package com.dudencov.happyhabit.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun DetailScreenNode(navController: NavHostController, viewModel: DetailViewModel) {
    val state by viewModel.state.collectAsState()

    HandleArgs(navController, viewModel)
    HandleSideEffects(viewModel, navController)

    DetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleArgs(
    navController: NavHostController,
    detailViewModel: DetailViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntry

    val itemId = navBackStackEntry?.arguments?.getString(Routes.Detail.HABIT_ID_ARG)?.toInt() ?: return
    val itemName = navBackStackEntry.arguments?.getString(Routes.Detail.HABIT_NAME_ARG) ?: return

    LaunchedEffect(itemId) {
        detailViewModel.onIntent(DetailIntent.SetHabitNameAndId(id = itemId, name = itemName))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: DetailViewModel,
    navController: NavHostController
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                DetailSideEffect.RouteBack -> navController.navigateUp()
            }
        }
    }
}
