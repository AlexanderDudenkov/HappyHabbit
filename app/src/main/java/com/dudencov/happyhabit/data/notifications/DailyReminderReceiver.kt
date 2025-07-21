package com.dudencov.happyhabit.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class DailyReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var scheduler: HabitNotificationScheduler

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("DailyReminderReceiver", "onReceive called!")
        val id = intent?.getIntExtra(HABIT_ID_EXTRA, -1) ?: return
        val hour = intent.getIntExtra(HABIT_HOUR_EXTRA, 9)
        val minute = intent.getIntExtra(HABIT_MINUTE_EXTRA, 0)

        notificationHelper.showHabitReminderNotification(id)

        val time = LocalTime(hour, minute)
        scheduler.scheduleNotification(time, id)
    }
}
