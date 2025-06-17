package com.dudencov.happyhabit.presentation.notification

import kotlinx.datetime.LocalTime

data class NotificationItemUi(
    val id: Int,
    val name: String,
    val reminderTime: LocalTime? = null
) 