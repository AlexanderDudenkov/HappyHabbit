package com.dudencov.happyhabit.domain.entities

import kotlinx.datetime.LocalTime

data class Habit(
    val id: Int = 0,
    val name: String = "",
    val reminderTime: LocalTime? = null
)