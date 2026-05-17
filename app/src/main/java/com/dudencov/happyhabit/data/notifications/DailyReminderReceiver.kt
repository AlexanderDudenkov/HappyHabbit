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
        Log.d("DailyReminderReceiver", "onReceive called! Intent action: ${intent?.action}")
        
        if (intent == null) {
            Log.e("DailyReminderReceiver", "Intent is null, cannot process notification")
            return
        }
        
        val id = intent.getIntExtra(HABIT_ID_EXTRA, -1)
        if (id == -1) {
            Log.e("DailyReminderReceiver", "Invalid habit ID received: $id")
            return
        }
        
        val hour = intent.getIntExtra(HABIT_HOUR_EXTRA, 9)
        val minute = intent.getIntExtra(HABIT_MINUTE_EXTRA, 0)

        Log.d("DailyReminderReceiver", "Processing notification for habitId=$id at $hour:$minute")

        try {
            notificationHelper.showHabitReminderNotification(id)
            Log.d("DailyReminderReceiver", "Notification shown successfully for habitId=$id")
        } catch (e: Exception) {
            Log.e("DailyReminderReceiver", "Failed to show notification for habitId=$id", e)
        }

        try {
            val time = LocalTime(hour, minute)
            scheduler.scheduleNotification(time, id)
            Log.d("DailyReminderReceiver", "Next alarm scheduled for habitId=$id at $time")
        } catch (e: Exception) {
            Log.e("DailyReminderReceiver", "Failed to reschedule notification for habitId=$id", e)
        }
    }
}
