package com.dudencov.happyhabit.presentation.entities

import com.dudencov.happyhabit.domain.entities.Habit

data class HabitUi(
    val id: Int = 0,
    val name: String = "",
)

fun HabitUi.toHabit() = Habit(
    id = id,
    name = name,
)

fun Habit.toHabitUi() = HabitUi(
    id = id,
    name = name,
)