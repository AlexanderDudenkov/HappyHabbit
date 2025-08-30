package com.dudencov.happyhabit.feature.deleteconfirmationdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dudencov.happyhabit.core.ui.R as UiR

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(UiR.string.delete_confirmation_dialog_title),
            )
        },
        text = {
            Text(
                text = stringResource(UiR.string.delete_confirmation_dialog_message),
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(stringResource(UiR.string.delete_confirmation_dialog_confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(stringResource(UiR.string.delete_confirmation_dialog_cancel))
            }
        }
    )
} 