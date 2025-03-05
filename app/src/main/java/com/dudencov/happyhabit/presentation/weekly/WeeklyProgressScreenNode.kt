package com.dudencov.happyhabit.presentation.weekly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController

@Composable
fun WeeklyProgressScreenNode(navController: NavHostController) {
    val viewModel: WeeklyProgressViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    HandleLifecycle(viewModel)
    HandleSideEffects(viewModel, navController)

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
    navController: NavHostController
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                WeeklySideEffect.RouteBack -> navController.navigateUp()
            }
        }
    }
}