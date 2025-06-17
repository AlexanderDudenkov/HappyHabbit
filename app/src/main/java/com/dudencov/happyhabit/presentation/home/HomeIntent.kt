package com.dudencov.happyhabit.presentation.home

sealed interface HomeIntent {
    data object OnResume : HomeIntent
    data object OnFabClicked : HomeIntent
    data object OnWeeklyProgressClicked : HomeIntent
    data object OnSettingsClicked : HomeIntent
    data class OnHabitClicked(val id: Int) : HomeIntent
    data class OnHabitEditClicked(val currentHabitId: Int) : HomeIntent
    data class OnHabitDeleteClicked(val id: Int) : HomeIntent
    data class OnHabitItemMenuClicked(val habitId: Int, val isExpended: Boolean) : HomeIntent
    data class OnHabitItemMenuDismissed(val habitId: Int) : HomeIntent
}