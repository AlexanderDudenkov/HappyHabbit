package com.dudencov.happyhabit.feature.weekly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dudencov.happyhabit.core.navigation.Navigator

@Composable
fun WeeklyProgressScreenNode(navigator: Navigator, viewModel: WeeklyProgressViewModel) {
    val state by viewModel.state.collectAsState()

    HandleLifecycle(viewModel)
    HandleSideEffects(viewModel, navigator)

    WeeklyProgressScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleLifecycle(viewModel: WeeklyProgressViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.onIntent(WeeklyProgressIntent.OnCreate)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: WeeklyProgressViewModel,
    navigator: Navigator
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                WeeklySideEffect.RouteBack -> navigator.navigateUp()
            }
        }
    }
}