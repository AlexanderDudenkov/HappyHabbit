package com.dudencov.happyhabit.presentation.home

import com.dudencov.happyhabit.presentation.habitdialog.HabitDialogTitle

sealed class HomeSideEffect {
    data class RouteToDetails(val habitId: String) : HomeSideEffect()
    data object RouteToWeeklyProgress : HomeSideEffect()

    data class RouteToDialog(val habitId: String? = null) : HomeSideEffect() {
        val title: HabitDialogTitle =
            if (habitId != null) HabitDialogTitle.EDIT else HabitDialogTitle.CREATE
    }
}