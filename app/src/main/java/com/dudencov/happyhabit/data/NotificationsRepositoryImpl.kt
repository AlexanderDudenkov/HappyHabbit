package com.dudencov.happyhabit.data

import android.content.Context
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import com.dudencov.happyhabit.data.db.entities.ReminderTimeEntity
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import com.dudencov.happyhabit.domain.entities.ReminderTime
import com.dudencov.happyhabit.domain.entities.toReminderTime
import kotlinx.datetime.LocalTime
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val reminderTimeDao: ReminderTimeDao,
    private val context: Context
) : NotificationsRepository {

    override suspend fun getAllReminders(): List<ReminderTime> {
        if (reminderTimeDao.isExist().not()) {
            reminderTimeDao.insert(
                entity = ReminderTimeEntity(name = context.getString(R.string.notification_settings_reminder))
            )
        }

        return reminderTimeDao.getAll()
            .map {
                ReminderTime(
                    id = it.id,
                    name = it.name,
                    reminderTime = it.reminderTime
                )
            }
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
        reminderTimeDao.updateReminderTimeById(id = id, reminderTime = reminderTime)
    }
}