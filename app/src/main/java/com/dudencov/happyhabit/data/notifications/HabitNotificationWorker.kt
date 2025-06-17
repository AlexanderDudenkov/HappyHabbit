package com.dudencov.happyhabit.data.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class HabitNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val id = inputData.getLong(REMINDER_ID_KEY, -1)
            val name = inputData.getString(REMINDER_NAME_KEY) ?: return@withContext Result.failure()
            
            // Show the notification
            notificationHelper.showHabitReminderNotification(id)
            
            // Return success to indicate the work completed successfully
            Result.success()
        } catch (e: Exception) {
            // Log the error and return failure
            Result.failure()
        }
    }

    companion object {
        const val REMINDER_ID_KEY = "reminder_id"
        const val REMINDER_NAME_KEY = "reminder_name"
    }
} 