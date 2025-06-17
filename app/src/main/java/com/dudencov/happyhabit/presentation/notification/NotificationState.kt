package com.dudencov.happyhabit.presentation.notification

data class NotificationState(
    val items: List<NotificationItemUi> = emptyList(),
    val isLoading: Boolean = false
) 