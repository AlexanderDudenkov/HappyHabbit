package com.dudencov.happyhabit.presentation.home

sealed interface HomeSideEffect {
    data class RouteToDetails(val habitId: Int) : HomeSideEffect
    data object RouteToWeeklyProgress : HomeSideEffect
    data object RouteToSettings : HomeSideEffect
    data class RouteToDialog(val habitId: Int? = null) : HomeSideEffect
    data class RouteToDeleteConfirmationDialog(val habitId: Int) : HomeSideEffect
}