package com.dudencov.happyhabit.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dudencov.happyhabit.domain.entities.ReminderTime
import kotlinx.datetime.LocalTime

@Entity(tableName = "ReminderTime")
data class ReminderTimeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "reminder_time", defaultValue = "'09:00:00'") val reminderTime: LocalTime = LocalTime(
        hour = 9,
        minute = 0,
        second = 0
    ),
    @ColumnInfo(name = "is_reminder_on", defaultValue = "0") val isOn: Boolean = false
)

fun ReminderTimeEntity.toReminderTime() = ReminderTime(
    id = id,
    name = name,
    reminderTime = reminderTime,
    isOn = isOn
)