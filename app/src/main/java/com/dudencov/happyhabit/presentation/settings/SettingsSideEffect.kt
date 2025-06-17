package com.dudencov.happyhabit.presentation.settings

sealed interface SettingsSideEffect {
    data object RouteBack : SettingsSideEffect
    data object RouteToNotifications : SettingsSideEffect
} 