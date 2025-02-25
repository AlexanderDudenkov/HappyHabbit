package com.dudencov.happyhabit.presentation.entities

data class HabitItemUi(
    val habit: HabitUi = HabitUi(),
    val menuExpended: Boolean = false,
)