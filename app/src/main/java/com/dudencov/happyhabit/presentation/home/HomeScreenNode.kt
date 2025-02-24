package com.dudencov.happyhabit.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun HomeScreenNode(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeState by viewModel.state.collectAsState()

    HandleArgs(navController, viewModel)
    HandleSideEffects(viewModel, navController)

    HomeScreen(
        state = homeState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleArgs(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = navBackStackEntry?.savedStateHandle
    val savedHabitIdState: State<String?> = savedStateHandle?.getStateFlow<String?>(
        key = Routes.Home.HABIT_ID_RECEIVE_RESULT,
        initialValue = null
    )?.collectAsState() ?: return

    if (savedHabitIdState.value != null) {
        LaunchedEffect(savedHabitIdState) {
            savedHabitIdState.value?.let {
                viewModel.onIntent(HomeIntent.OnHabitSaved(it))
                savedStateHandle.remove<String>(Routes.Home.HABIT_ID_RECEIVE_RESULT)
            }
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
            }
        }
    }
}
