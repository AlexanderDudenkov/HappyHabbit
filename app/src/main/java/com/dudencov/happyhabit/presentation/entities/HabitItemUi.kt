package com.dudencov.happyhabit.presentation.entities

import androidx.compose.runtime.Stable

@Stable
data class HabitItemUi(
    val habit: HabitUi = HabitUi(),
    val menuExpended: Boolean = false,
)