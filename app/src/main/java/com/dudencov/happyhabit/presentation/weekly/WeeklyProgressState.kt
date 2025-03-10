package com.dudencov.happyhabit.presentation.weekly

val stub = WeeklyProgressState(
    habits = List(50) { i ->
        WeeklyHabitUi(
            id = i,
            name = "habit",
            days = List(7) { i2 -> if (i2 % 2 == 0) WeeklyDayUi(true) else WeeklyDayUi() }
        )
    })

data class WeeklyProgressState(
    val habits: List<WeeklyHabitUi> = emptyList()
)

data class WeeklyHabitUi(
    val id: Int = 0,
    val name: String = "",
    val days: List<WeeklyDayUi> = emptyList()
)

data class WeeklyDayUi(
    val isSelected: Boolean = false
)