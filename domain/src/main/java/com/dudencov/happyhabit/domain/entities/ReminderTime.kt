package com.dudencov.happyhabit.domain.entities

import kotlinx.datetime.LocalTime

data class ReminderTime(
    val id: Int = 0,
    val name: String = "",
    val reminderTime: LocalTime = LocalTime(hour = 9, minute = 0, second = 0),
    val isOn: Boolean = false
)
