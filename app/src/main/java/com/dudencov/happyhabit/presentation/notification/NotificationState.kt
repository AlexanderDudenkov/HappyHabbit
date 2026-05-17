package com.dudencov.happyhabit.presentation.notification

data class NotificationState(
    val items: List<NotificationItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val canScheduleExactAlarms: Boolean = true,
    val isIgnoringBatteryOptimizations: Boolean = true,
    val recommendations: List<String> = emptyList()
) 