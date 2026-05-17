package com.dudencov.happyhabit.data.notifications

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class BatteryOptimizationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        val isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        Log.d("BatteryOptimization", "Is ignoring battery optimizations: $isIgnoring")
        return isIgnoring
    }

    fun createBatterySettingsIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()
        }
    }

    fun canScheduleExactAlarms(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val canSchedule = alarmManager.canScheduleExactAlarms()
            Log.d("BatteryOptimization", "Can schedule exact alarms: $canSchedule")
            return canSchedule
        }
        return true
    }

    fun createExactAlarmPermissionIntent(): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = "package:${context.packageName}".toUri()
            }
        } else {
            Intent(Settings.ACTION_SETTINGS)
        }
    }
}
