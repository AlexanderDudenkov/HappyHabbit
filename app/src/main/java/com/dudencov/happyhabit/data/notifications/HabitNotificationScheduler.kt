package com.dudencov.happyhabit.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

const val HABIT_ID_EXTRA = "habit_id_extra"
const val HABIT_HOUR_EXTRA = "habit_hour_extra"
const val HABIT_MINUTE_EXTRA = "habit_minute_extra"

@Singleton
class HabitNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun scheduleNotification(
        reminderTime: LocalTime,
        reminderId: Int
    ) {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()

        val today: LocalDate = now.toLocalDateTime(timeZone).date
        val currentDateTime: LocalDateTime = now.toLocalDateTime(timeZone)

        var targetDate = today
        val todayReminderDateTime = LocalDateTime(today, reminderTime)

        if (todayReminderDateTime <= currentDateTime) {
            targetDate = today.plus(1, DateTimeUnit.DAY)
        }

        val targetDateTime = LocalDateTime(date = targetDate, time = reminderTime)
        val triggerMillis = targetDateTime.toInstant(timeZone).toEpochMilliseconds()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            createPendingIntent(reminderTime = reminderTime, reminderId = reminderId)
        )

        Log.d(
            "HabitNotificationScheduler",
            "Next alarm scheduled for: $targetDateTime ($triggerMillis)"
        )
    }

    fun cancelNotification(reminderTime: LocalTime, reminderId: Int) {
        alarmManager.cancel(
            createPendingIntent(reminderTime = reminderTime, reminderId = reminderId)
        )

        Log.d("HabitNotificationScheduler", "Alarm canceled for habitId: $reminderId")
    }

    private fun createPendingIntent(
        reminderTime: LocalTime,
        reminderId: Int
    ): PendingIntent {
        val intent = Intent(context, DailyReminderReceiver::class.java).apply {
            putExtra(HABIT_ID_EXTRA, reminderId)
            putExtra(HABIT_HOUR_EXTRA, reminderTime.hour)
            putExtra(HABIT_MINUTE_EXTRA, reminderTime.minute)
        }

        return PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

}
