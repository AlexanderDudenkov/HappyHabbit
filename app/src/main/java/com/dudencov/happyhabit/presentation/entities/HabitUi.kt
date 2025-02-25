package com.dudencov.happyhabit.presentation.entities

import com.dudencov.happyhabit.data.entities.Habit

data class HabitUi(
    val id: String = "",
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