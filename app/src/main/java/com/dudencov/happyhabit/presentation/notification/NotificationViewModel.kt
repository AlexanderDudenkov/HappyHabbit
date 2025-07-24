package com.dudencov.happyhabit.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.data.notifications.HabitNotificationScheduler
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationsRepository,
    private val notificationScheduler: HabitNotificationScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationSideEffect>()
    val sideEffect: SharedFlow<NotificationSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: NotificationIntent) {
        viewModelScope.launch {
            when (intent) {
                NotificationIntent.OnNavigateBack -> {
                    _sideEffect.emit(NotificationSideEffect.RouteBack)
                }

                NotificationIntent.OnCreate -> {
                    loadData()
                }

                is NotificationIntent.OnSetReminderTime -> {
                    setReminderTime(intent.habitId, intent.time)
                }

                is NotificationIntent.OnSwitchItem -> {
                    val item = state.value.items.find { it.id == intent.habitId }

                    item?.let { item ->
                        if (intent.currentValue) {
                            notificationScheduler.cancelNotification(
                                reminderTime = item.reminderTime,
                                reminderId = intent.habitId
                            )
                        } else {
                            notificationScheduler.scheduleNotification(
                                reminderId = intent.habitId,
                                reminderTime = item.reminderTime
                            )
                        }
                    }

                    withContext(Dispatchers.IO) {
                        item?.id?.let {
                            repository.updateIsReminderOnById(
                                id = it,
                                value = !intent.currentValue
                            )
                        }
                    }

                    _state.update {
                        it.copy(items = it.items.map { item ->
                            if (item.id == intent.habitId) {
                                item.copy(isSwitchOn = !item.isSwitchOn)
                            } else item
                        })
                    }
                }
            }
        }
    }

    private suspend fun loadData() {
        val reminders = repository.getAllReminders().map { habit ->
            NotificationItemUi(
                id = habit.id,
                name = habit.name,
                reminderTime = habit.reminderTime,
                isSwitchOn = habit.isOn
            )
        }

        _state.update {
            it.copy(items = reminders)
        }
    }

    private suspend fun setReminderTime(id: Int, time: LocalTime) {
        repository.updateReminderTimeById(id, time)

        _state.update { state ->
            state.copy(
                items = state.items.map {
                    if (it.id == id) {
                        it.copy(reminderTime = time)
                    } else {
                        it
                    }
                }
            )
        }

        notificationScheduler.scheduleNotification(reminderId = id, reminderTime = time)
    }
} 