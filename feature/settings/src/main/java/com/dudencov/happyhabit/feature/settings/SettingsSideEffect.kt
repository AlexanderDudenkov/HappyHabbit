package com.dudencov.happyhabit.feature.settings

sealed interface SettingsSideEffect {
    data object RouteBack : SettingsSideEffect
    data object RouteToNotifications : SettingsSideEffect
} 