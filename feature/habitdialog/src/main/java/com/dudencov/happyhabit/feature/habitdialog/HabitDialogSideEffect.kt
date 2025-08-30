package com.dudencov.happyhabit.feature.habitdialog

sealed class HabitDialogSideEffect {
    data object OnDismiss : HabitDialogSideEffect()
}