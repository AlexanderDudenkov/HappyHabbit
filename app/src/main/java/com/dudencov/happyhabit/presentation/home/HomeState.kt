package com.dudencov.happyhabit.presentation.home

import androidx.compose.runtime.Stable
import com.dudencov.happyhabit.presentation.entities.HabitUi

val previewStub = List(11) { HabitItemUi(HabitUi("$it", "habit $it")) }

data class HomeState(
    val isEmptyStateVisible: Boolean = true,
    val isWeeklyEnabled: Boolean = false,
    val habitItems: List<HabitItemUi> = emptyList(),
) {
    fun updateItemMenuExpandState(habitId: String, isExpanded: Boolean): HomeState {
        return copy(habitItems = habitItems.map {
            if (it.habit.id == habitId) it.copy(menuExpended = isExpanded) else it
        })
    }
}

@Stable
data class HabitItemUi(
    val habit: HabitUi = HabitUi(),
    val menuExpended: Boolean = false,
)