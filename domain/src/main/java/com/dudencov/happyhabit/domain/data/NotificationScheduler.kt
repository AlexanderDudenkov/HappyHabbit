package com.dudencov.happyhabit.domain.data

import kotlinx.datetime.LocalTime

interface NotificationScheduler {
    fun scheduleNotification(reminderTime: LocalTime, reminderId: Int)
    fun cancelNotification(reminderTime: LocalTime, reminderId: Int)
}
