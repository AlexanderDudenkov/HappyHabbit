package com.dudencov.happyhabit.feature.notification

data class NotificationState(
    val items: List<NotificationItemUi> = emptyList(),
    val isLoading: Boolean = false
) 