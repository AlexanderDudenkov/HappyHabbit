package com.dudencov.happyhabit.domain.data

import com.dudencov.happyhabit.domain.entities.ReminderTime
import kotlinx.datetime.LocalTime

interface NotificationsRepository {

    suspend fun getAllReminders(): List<ReminderTime>

    suspend fun isRemindersExist(): Boolean

    suspend fun getReminder(id: Int): ReminderTime

    suspend fun updateReminderTimeById(id: Int, reminderTime: LocalTime?)

    suspend fun scheduleNotification(reminderTime: LocalTime, reminderId: Int)

    suspend fun scheduleIfOnNotification(reminderTime: LocalTime, reminderId: Int)

    suspend fun cancelAndOffNotification(reminderTime: LocalTime, reminderId: Int)
}