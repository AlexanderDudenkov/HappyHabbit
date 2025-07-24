package com.dudencov.happyhabit.presentation.notification

import kotlinx.datetime.LocalTime

sealed interface NotificationIntent {
    data object OnNavigateBack : NotificationIntent
    data object OnCreate : NotificationIntent
    data class OnSetReminderTime(val habitId: Int, val time: LocalTime) : NotificationIntent
    data class OnSwitchItem(val habitId: Int, val currentValue: Boolean) : NotificationIntent
} 