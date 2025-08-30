package com.dudencov.happyhabit.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dudencov.happyhabit.core.navigation.Navigator
import com.dudencov.happyhabit.core.navigation.Routes

@Composable
fun SettingsScreenNode(navigator: Navigator, viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsState()

    HandleSideEffects(viewModel, navigator)

    SettingsScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleSideEffects(
    viewModel: SettingsViewModel,
    navigator: Navigator
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                SettingsSideEffect.RouteBack -> navigator.navigateUp()
                SettingsSideEffect.RouteToNotifications -> navigator.navigateTo(Routes.Notification.ROUTE_PATTERN)
            }
        }
    }
}