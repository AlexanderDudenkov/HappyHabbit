package com.dudencov.happyhabit.presentation.deleteconfirmationdialog

sealed class DeleteConfirmationDialogIntent {
    data class OnSetHabitId(val habitId: Int) : DeleteConfirmationDialogIntent()
    data object OnConfirm : DeleteConfirmationDialogIntent()
    data object OnDismiss : DeleteConfirmationDialogIntent()
} 