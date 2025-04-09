package com.dudencov.happyhabit.presentation.home

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
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun HomeScreenNode(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeState by viewModel.state.collectAsState()

    HandleLifecycle(viewModel)
    HandleSideEffects(viewModel, navController)

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
    navController: NavHostController
) {
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.RouteToDetails -> {
                    navController.navigate(Routes.Detail.createRoute(effect.habitId))
                }

                HomeSideEffect.RouteToWeeklyProgress -> {
                    navController.navigate(Routes.WeeklyProgress.WEEKLY)
                }

                is HomeSideEffect.RouteToDialog -> {
                    navController.navigate(Routes.HabitDialog.createRoute(effect.habitId))
                }

                is HomeSideEffect.RouteToDeleteConfirmationDialog -> {
                    navController.navigate(Routes.DeleteConfirmationDialog.createRoute(effect.habitId))
                }
            }
        }
    }
}
