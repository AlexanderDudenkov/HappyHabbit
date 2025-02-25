package com.dudencov.happyhabit.presentation.habitdialog

sealed class HabitDialogSideEffect {
    data object OnDismiss : HabitDialogSideEffect()
}