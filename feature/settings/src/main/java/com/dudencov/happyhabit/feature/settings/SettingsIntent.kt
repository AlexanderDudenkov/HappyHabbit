package com.dudencov.happyhabit.feature.settings

sealed interface SettingsIntent {
    data object OnNavigateBack : SettingsIntent
    data object OnNotificationsClicked : SettingsIntent
} 