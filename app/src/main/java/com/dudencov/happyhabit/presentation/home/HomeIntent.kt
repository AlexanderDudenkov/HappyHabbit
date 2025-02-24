package com.dudencov.happyhabit.presentation.home

sealed interface HomeIntent {
    data class OnHabitClicked(val habitId: String) : HomeIntent
    data object OnFabClicked : HomeIntent
    data class OnHabitEditClicked(val currentHabitId: String) : HomeIntent
    data class OnHabitDeleteClicked(val id: String) : HomeIntent
    data object OnWeeklyProgressClicked : HomeIntent
    data class OnHabitSaved(val newHabitId: String) : HomeIntent
}