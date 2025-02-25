package com.dudencov.happyhabit.presentation.home

import com.dudencov.happyhabit.presentation.entities.HabitItemUi
import com.dudencov.happyhabit.presentation.entities.HabitUi

val previewStub = List(11) { HabitItemUi(HabitUi("$it", "habit $it")) }

data class HomeState(
    val emptyStateVisible: Boolean = true,
    val habitItems: List<HabitItemUi> = emptyList(),
) {
    fun updateItemMenuExpandState(habitId: String, isExpanded: Boolean): HomeState {
        return copy(habitItems = habitItems.map {
            if (it.habit.id == habitId) it.copy(menuExpended = isExpanded) else it
        })
    }
}