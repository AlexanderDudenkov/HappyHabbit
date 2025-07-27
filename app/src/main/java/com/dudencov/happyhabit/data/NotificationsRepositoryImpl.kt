package com.dudencov.happyhabit.data

import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import com.dudencov.happyhabit.data.db.entities.ReminderTimeEntity
import com.dudencov.happyhabit.data.notifications.HabitNotificationScheduler
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import com.dudencov.happyhabit.domain.entities.ReminderTime
import com.dudencov.happyhabit.domain.entities.toReminderTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val reminderTimeDao: ReminderTimeDao,
    private val notificationScheduler: HabitNotificationScheduler
) : NotificationsRepository {

    override suspend fun getAllReminders(): List<ReminderTime> {
        if (reminderTimeDao.isExist().not()) {
            reminderTimeDao.insert(
                entity = ReminderTimeEntity()
            )
        }
        return reminderTimeDao.getAll()
            .map { it.toReminderTime() }
    }

    override suspend fun isRemindersExist(): Boolean {
        return reminderTimeDao.isExist()
    }

    override suspend fun getReminder(id: Int): ReminderTime {
        return reminderTimeDao.getReminder(id)?.toReminderTime() ?: ReminderTime()
    }

    override suspend fun updateReminderTimeById(
        id: Int,
        reminderTime: LocalTime?
    ) {
        withContext(Dispatchers.IO) {
            reminderTimeDao.updateReminderTimeById(id = id, reminderTime = reminderTime)
        }
    }

    override suspend fun scheduleNotification(
        reminderTime: LocalTime,
        reminderId: Int
    ) {
        withContext(Dispatchers.IO) {
            reminderTimeDao.updateIsReminderOnById(id = reminderId, isOn = true)
        }
        notificationScheduler.scheduleNotification(
            reminderId = reminderId,
            reminderTime = reminderTime
        )
    }

    override suspend fun scheduleIfOnNotification(
        reminderTime: LocalTime,
        reminderId: Int
    ) {
        val isNotificationOn: Boolean = withContext(Dispatchers.IO) {
            reminderTimeDao.getIsReminderOn(reminderId) ?: false
        }
        if (isNotificationOn) {
            notificationScheduler.scheduleNotification(
                reminderId = reminderId,
                reminderTime = reminderTime
            )
        }
    }

    override suspend fun cancelAndOffNotification(
        reminderTime: LocalTime,
        reminderId: Int
    ) {
        withContext(Dispatchers.IO) {
            reminderTimeDao.updateIsReminderOnById(id = reminderId, isOn = false)
        }

        notificationScheduler.cancelNotification(
            reminderTime = reminderTime,
            reminderId = reminderId
        )
    }
}