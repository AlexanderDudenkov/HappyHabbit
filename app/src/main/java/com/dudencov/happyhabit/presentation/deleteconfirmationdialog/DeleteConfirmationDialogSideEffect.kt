package com.dudencov.happyhabit.presentation.deleteconfirmationdialog

sealed class DeleteConfirmationDialogSideEffect {
    data object OnDismiss : DeleteConfirmationDialogSideEffect()
} 