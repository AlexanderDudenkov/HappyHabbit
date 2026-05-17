package com.dudencov.happyhabit.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationsRepository: NotificationsRepository

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootCompletedReceiver", "Device booted, rescheduling notifications")

            val pendingResult = goAsync()
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

            scope.launch {
                try {
                    val reminders = notificationsRepository.getAllReminders()
                    reminders.forEach { reminder ->
                        notificationsRepository.scheduleIfOnNotification(
                            reminderTime = reminder.reminderTime,
                            reminderId = reminder.id
                        )
                        Log.d(
                            "BootCompletedReceiver",
                            "Rescheduled reminder ${reminder.id} for ${reminder.reminderTime}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("BootCompletedReceiver", "Error rescheduling notifications", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
