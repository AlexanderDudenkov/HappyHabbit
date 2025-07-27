package com.dudencov.happyhabit.presentation.home

sealed interface HomeSideEffect {
    data class RouteToDetails(val habitId: Int, val habitName: String) : HomeSideEffect
    data object RouteToWeeklyProgress : HomeSideEffect
    data object RouteToSettings : HomeSideEffect
    data class RouteToDialog(val habitId: Int? = null) : HomeSideEffect
    data class RouteToDeleteConfirmationDialog(val habitId: Int) : HomeSideEffect
}