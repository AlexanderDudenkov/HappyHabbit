package com.dudencov.happyhabit.presentation.habitdialog

sealed class HabitDialogSideEffect {
    data class SaveAndDismiss(val habitId: String) : HabitDialogSideEffect()
    data object OnDismiss : HabitDialogSideEffect()
}