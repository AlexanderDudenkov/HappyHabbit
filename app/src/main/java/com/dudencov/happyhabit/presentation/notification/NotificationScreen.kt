package com.dudencov.happyhabit.presentation.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import kotlinx.datetime.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    state: NotificationState,
    onIntent: (NotificationIntent) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(scrollBehavior, onIntent) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HabitList(state, onIntent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (NotificationIntent) -> Unit
) {
    val context = LocalContext.current

    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        TopAppBar(
            modifier = Modifier.testTag(NotificationTestTags.TOP_APP_BAR.tag),
            scrollBehavior = scrollBehavior,
            title = { Text(text = context.getString(R.string.notification_settings_title)) },
            navigationIcon = {
                IconButton(onClick = { onIntent(NotificationIntent.OnNavigateBack) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.button_back_content_desk),
                    )
                }
            }
        )
    }
}

@Composable
private fun HabitList(
    state: NotificationState,
    onIntent: (NotificationIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
    ) {
        items(
            items = state.items,
            key = { it.id }) { habit ->
            HabitNotificationItem(habit, onIntent)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitNotificationItem(
    habit: NotificationItemUi,
    onIntent: (NotificationIntent) -> Unit
) {
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = habit.reminderTime?.hour ?: 9,
        initialMinute = habit.reminderTime?.minute ?: 0
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.notification_settings_reminder),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.reminder_time_content_desc)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = habit.reminderTime?.let {
                    String.format(Locale.getDefault(), "%02d:%02d", it.hour, it.minute)
                } ?: context.getString(R.string.empty_time),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                onIntent(NotificationIntent.OnSetReminderTime(habit.id, LocalTime(hour, minute)))
                showTimePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.select_reminder_time)) },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
} 