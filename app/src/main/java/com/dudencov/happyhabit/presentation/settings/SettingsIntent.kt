package com.dudencov.happyhabit.presentation.settings

sealed interface SettingsIntent {
    data object OnNavigateBack : SettingsIntent
    data object OnNotificationsClicked : SettingsIntent
} 