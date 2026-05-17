package com.dudencov.happyhabit.data.notifications

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationDebugHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val batteryOptimizationHelper: BatteryOptimizationHelper
) {

    fun logSystemStatus() {
        Log.d("NotificationDebug", "========== NOTIFICATION SYSTEM STATUS ==========")
        Log.d("NotificationDebug", "Android Version: ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE})")
        Log.d("NotificationDebug", "Device Manufacturer: ${Build.MANUFACTURER}")
        Log.d("NotificationDebug", "Device Model: ${Build.MODEL}")
        
        val canScheduleExact = batteryOptimizationHelper.canScheduleExactAlarms()
        Log.d("NotificationDebug", "Can schedule exact alarms: $canScheduleExact")
        
        val isIgnoringBattery = batteryOptimizationHelper.isIgnoringBatteryOptimizations()
        Log.d("NotificationDebug", "Ignoring battery optimizations: $isIgnoringBattery")
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            try {
                val nextAlarmClock = alarmManager.nextAlarmClock
                Log.d("NotificationDebug", "Next system alarm: ${nextAlarmClock?.triggerTime}")
            } catch (e: Exception) {
                Log.e("NotificationDebug", "Error getting next alarm", e)
            }
        }
        
        Log.d("NotificationDebug", "===============================================")
    }

    fun getRecommendations(): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (!batteryOptimizationHelper.canScheduleExactAlarms()) {
            recommendations.add("⚠️ Cannot schedule exact alarms. Go to Settings → Apps → HappyHabbit → Alarms & reminders → Allow")
        }
        
        if (!batteryOptimizationHelper.isIgnoringBatteryOptimizations()) {
            recommendations.add("⚠️ Battery optimization is ON. This may prevent timely notifications")
        }
        
        if (Build.MANUFACTURER.equals("xiaomi", ignoreCase = true) ||
            Build.MANUFACTURER.equals("huawei", ignoreCase = true) ||
            Build.MANUFACTURER.equals("oppo", ignoreCase = true) ||
            Build.MANUFACTURER.equals("vivo", ignoreCase = true)) {
            recommendations.add("⚠️ ${Build.MANUFACTURER} device detected. Check for additional battery/autostart settings in device settings")
        }
        
        return recommendations
    }
}
