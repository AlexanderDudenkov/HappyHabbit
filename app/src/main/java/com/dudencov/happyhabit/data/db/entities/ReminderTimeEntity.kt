package com.dudencov.happyhabit.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalTime

@Entity(tableName = "ReminderTime")
data class ReminderTimeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "reminder_time") val reminderTime: LocalTime = LocalTime(
        hour = 9,
        minute = 0,
        second = 0
    ),
    @ColumnInfo(name = "is_reminder_on") val isOn: Boolean = false
)