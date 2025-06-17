package com.dudencov.happyhabit.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dudencov.happyhabit.presentation.navigation.Routes

@Composable
fun SettingsScreenNode(navController: NavHostController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    HandleSideEffects(viewModel, navController)

    SettingsScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleSideEffects(
    viewModel: SettingsViewModel,
    navController: NavHostController
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                SettingsSideEffect.RouteBack -> navController.navigateUp()
                SettingsSideEffect.RouteToNotifications -> navController.navigate(Routes.Notification.ROUTE_PATTERN)
            }
        }
    }
}