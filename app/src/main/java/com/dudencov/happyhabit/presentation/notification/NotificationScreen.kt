package com.dudencov.happyhabit.presentation.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.presentation.notification.NotificationIntent.OnSwitchItem
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme
import kotlinx.datetime.LocalTime

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
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        TopAppBar(
            modifier = Modifier.testTag(NotificationTestTags.TOP_APP_BAR.tag),
            scrollBehavior = scrollBehavior,
            title = { Text(text = stringResource(R.string.notification_settings_title)) },
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
        if (state.recommendations.isNotEmpty()) {
            item {
                PermissionWarningCard(
                    recommendations = state.recommendations,
                    canScheduleExactAlarms = state.canScheduleExactAlarms,
                    isIgnoringBatteryOptimizations = state.isIgnoringBatteryOptimizations,
                    onIntent = onIntent
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        items(
            items = state.items,
            key = { it.id }) { habit ->
            HabitNotificationItem(habit, onIntent)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PermissionWarningCard(
    recommendations: List<String>,
    canScheduleExactAlarms: Boolean,
    isIgnoringBatteryOptimizations: Boolean,
    onIntent: (NotificationIntent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Notification Setup Required",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            recommendations.forEach { recommendation ->
                Text(
                    text = recommendation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!canScheduleExactAlarms) {
                    Button(
                        onClick = { onIntent(NotificationIntent.OnRequestExactAlarmPermission) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Fix Alarms")
                    }
                }

                if (!isIgnoringBatteryOptimizations) {
                    Button(
                        onClick = { onIntent(NotificationIntent.OnRequestBatteryOptimization) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Battery Settings")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HabitNotificationItem(
    itemState: NotificationItemUi,
    onIntent: (NotificationIntent) -> Unit
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = itemState.reminderTime.hour,
        initialMinute = itemState.reminderTime.minute
    )
    val configuration = LocalConfiguration.current

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

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = itemState.reminderTime.let {
                    String.format(
                        configuration.locales[0],
                        "%02d:%02d",
                        it.hour,
                        it.minute
                    )
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Switch(
                itemState.isSwitchOn,
                onCheckedChange = {
                    onIntent(
                        OnSwitchItem(
                            habitId = itemState.id,
                            currentValue = itemState.isSwitchOn
                        )
                    )
                })
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                onIntent(
                    NotificationIntent.OnSetReminderTime(
                        itemState.id,
                        LocalTime(hour, minute)
                    )
                )
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

@Composable
@PreviewScreenSizes
@Preview(showSystemUi = false, showBackground = true)
private fun PreviewState() {
    HappyHabitTheme {
        NotificationScreen(
            state = NotificationState(items = listOf(NotificationItemUi(id = 1, name = "name"))),
            onIntent = {},
        )
    }
}