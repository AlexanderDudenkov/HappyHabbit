package com.dudencov.happyhabit.presentation.deleteconfirmationdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dudencov.happyhabit.R

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.delete_confirmation_dialog_title),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_confirmation_dialog_message),
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(stringResource(R.string.delete_confirmation_dialog_confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(stringResource(R.string.delete_confirmation_dialog_cancel))
            }
        }
    )
} 