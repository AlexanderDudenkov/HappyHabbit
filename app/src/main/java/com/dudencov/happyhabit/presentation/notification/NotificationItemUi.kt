package com.dudencov.happyhabit.presentation.notification

import kotlinx.datetime.LocalTime

data class NotificationItemUi(
    val id: Int,
    val name: String,
    val reminderTime: LocalTime = LocalTime(hour = 9, minute = 0, second = 0),
    val isSwitchOn: Boolean = false
) 