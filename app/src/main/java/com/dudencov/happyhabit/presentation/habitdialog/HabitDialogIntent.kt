package com.dudencov.happyhabit.presentation.habitdialog

sealed class HabitDialogIntent {
    data class OnChangeTitle(val title: HabitDialogTitle) : HabitDialogIntent()
    data class OnSetHabitToTextField(val habitId: String) : HabitDialogIntent()
    data class OnTextChanged(val text: String) : HabitDialogIntent()
    data object OnSave : HabitDialogIntent()
    data object OnCancel : HabitDialogIntent()
}