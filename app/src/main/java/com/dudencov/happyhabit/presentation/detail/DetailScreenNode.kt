package com.dudencov.happyhabit.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun DetailScreenNode(navController: NavHostController) {
    val viewModel: DetailViewModel = hiltViewModel()
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

    val itemId = navBackStackEntry?.arguments?.getString(Routes.Detail.HABIT_ID_ARG) ?: return

    LaunchedEffect(itemId) {
        detailViewModel.onIntent(DetailIntent.SetHabitId(itemId))
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
