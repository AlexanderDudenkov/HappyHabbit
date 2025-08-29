package com.dudencov.happyhabit.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dudencov.happyhabit.core.navigation.Navigator
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun HomeScreenNode(navigator: Navigator, viewModel: HomeViewModel) {
    val homeState by viewModel.state.collectAsState()

    HandleLifecycle(viewModel)
    HandleSideEffects(viewModel, navigator)

    HomeScreen(
        state = homeState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleLifecycle(viewModel: HomeViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onIntent(HomeIntent.OnResume)
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
    viewModel: HomeViewModel,
    navigator: Navigator
) {
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.RouteToDetails -> {
                    navigator.navigateTo(
                        Routes.Detail.createRoute(
                            habitId = effect.habitId,
                            habitName = effect.habitName
                        )
                    )
                }

                HomeSideEffect.RouteToWeeklyProgress -> {
                    navigator.navigateTo(Routes.WeeklyProgress.ROUTE_PATTERN)
                }

                HomeSideEffect.RouteToSettings -> {
                    navigator.navigateTo(Routes.Settings.ROUTE_PATTERN)
                }

                is HomeSideEffect.RouteToDialog -> {
                    navigator.navigateTo(Routes.HabitDialog.createRoute(effect.habitId))
                }

                is HomeSideEffect.RouteToDeleteConfirmationDialog -> {
                    navigator.navigateTo(Routes.DeleteConfirmationDialog.createRoute(effect.habitId))
                }
            }
        }
    }
}
