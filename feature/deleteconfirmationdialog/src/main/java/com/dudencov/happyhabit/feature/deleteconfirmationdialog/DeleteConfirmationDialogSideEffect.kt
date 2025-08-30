package com.dudencov.happyhabit.feature.deleteconfirmationdialog

sealed class DeleteConfirmationDialogSideEffect {
    data object OnDismiss : DeleteConfirmationDialogSideEffect()
} 