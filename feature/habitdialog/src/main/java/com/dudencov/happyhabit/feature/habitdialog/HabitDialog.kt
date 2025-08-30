package com.dudencov.happyhabit.feature.habitdialog

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.dudencov.happyhabit.core.ui.R as UiR

@Composable
fun HabitDialog(
    state: HabitDialogState,
    onIntent: (HabitDialogIntent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onIntent(HabitDialogIntent.OnCancel) },
        title = {
            Text(
                text = stringResource(state.title.stringResId),
                Modifier.testTag(DialogTestTags.TITLE.tag),
            )
        },
        text = {
            val localFocusManager = LocalFocusManager.current

            TextField(
                value = state.habitUi.name,
                onValueChange = { onIntent(HabitDialogIntent.OnTextChanged(it)) },
                placeholder = { Text(text = stringResource(UiR.string.habit_dialog_text_field_placeholder)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                    }
                ),
                isError = state.errorResId != null,
                supportingText = state.errorResId?.let { { Text(stringResource(it)) } },
                modifier = Modifier.testTag(DialogTestTags.TEXT_FIELD.tag)
            )
        },
        confirmButton = {
            Button(
                modifier = Modifier.testTag(DialogTestTags.BTN_SAVE.tag),
                onClick = { onIntent(HabitDialogIntent.OnSave) },
                enabled = state.saveEnabled
            ) {
                Text(stringResource(UiR.string.habit_dialog_save_btn_title))
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.testTag(DialogTestTags.BTN_CANCEL.tag),
                onClick = { onIntent(HabitDialogIntent.OnCancel) }) {
                Text(stringResource(UiR.string.habit_dialog_cancel_btn_title))
            }
        }
    )
}
