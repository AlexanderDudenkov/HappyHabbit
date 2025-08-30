package com.dudencov.happyhabit.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dudencov.happyhabit.core.navigation.Navigator
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun DetailScreenNode(navigator: Navigator, viewModel: DetailViewModel) {
    val state by viewModel.state.collectAsState()

    HandleArgs(navigator, viewModel)
    HandleSideEffects(viewModel, navigator)

    DetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleArgs(
    navigator: Navigator,
    detailViewModel: DetailViewModel
) {
    val itemId = navigator.getIntArgument(Routes.Detail.HABIT_ID_ARG) ?: return
    val itemName = navigator.getStringArgument(Routes.Detail.HABIT_NAME_ARG) ?: return

    LaunchedEffect(itemId) {
        detailViewModel.onIntent(DetailIntent.SetHabitNameAndId(id = itemId, name = itemName))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: DetailViewModel,
    navigator: Navigator
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                DetailSideEffect.RouteBack -> navigator.navigateUp()
            }
        }
    }
}
