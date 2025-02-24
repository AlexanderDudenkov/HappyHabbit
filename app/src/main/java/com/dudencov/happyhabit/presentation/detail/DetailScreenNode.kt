package com.dudencov.happyhabit.presentation.detail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
        detailViewModel.onIntent(DetailIntent.SetItemId(itemId))
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: DetailViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is DetailSideEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                DetailSideEffect.RouteHome -> navController.navigateUp()
            }
        }
    }
}
