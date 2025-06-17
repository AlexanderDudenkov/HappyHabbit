package com.dudencov.happyhabit.data.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dudencov.happyhabit.data.notifications.HabitNotificationWorker.Companion.REMINDER_ID_KEY
import com.dudencov.happyhabit.data.notifications.HabitNotificationWorker.Companion.REMINDER_NAME_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class HabitNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager by lazy { WorkManager.getInstance(context) }

    fun scheduleNotification(reminderId: Long, reminderName: String, reminderTime: LocalTime) {
        val now = kotlinx.datetime.Clock.System.now()
        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val targetDateTime = LocalDateTime(today, reminderTime)
        val targetInstant = targetDateTime.toInstant(TimeZone.currentSystemDefault())

        // If the time has already passed today, schedule for tomorrow
        val initialDelay = if (targetInstant > now) {
            targetInstant - now
        } else {
            targetInstant.plus(1.days) - now
        }

        val inputData = workDataOf(
            REMINDER_ID_KEY to reminderId,
            REMINDER_NAME_KEY to reminderName
        )

        val notificationWork = PeriodicWorkRequestBuilder<HabitNotificationWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay.inWholeMilliseconds, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "habit_reminder_$reminderId",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWork
        )
    }

    fun cancelNotification(reminderId: Long) {
        workManager.cancelUniqueWork("habit_reminder_$reminderId")
    }
} 