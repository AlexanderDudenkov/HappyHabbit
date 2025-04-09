package com.dudencov.happyhabit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.domain.data.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import javax.inject.Inject
import kotlinx.datetime.LocalDate as KtLocalDate

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DetailSideEffect>()
    val sideEffect: SharedFlow<DetailSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: DetailIntent) {
        viewModelScope.launch {
            when (intent) {
                is DetailIntent.SetHabitId -> {
                    _state.update {
                        it.copy(
                            habitId = intent.id,
                            selectedDates = repository.getHabitDates(habitId = intent.id),
                            calendarDataUi = calculateCalendarData(it.currentDate)
                        )
                    }
                }

                is DetailIntent.OnScreenSwiped -> {
                    _state.update {
                        val res = if (intent.direction == SwipeDirection.LEFT) {
                            it.currentDate.plus(1, DateTimeUnit.MONTH)
                        } else {
                            it.currentDate.minus(1, DateTimeUnit.MONTH)
                        }
                        it.copy(
                            currentDate = res, 
                            swipeDirection = intent.direction,
                            calendarDataUi = calculateCalendarData(res)
                        )
                    }
                }

                is DetailIntent.OnDateSelected -> {
                    val isAlreadySelected = state.value.selectedDates.contains(intent.date)

                    if (isAlreadySelected) {
                        repository.deleteDate(state.value.habitId, intent.date)
                    } else {
                        repository.createCurrentDate(state.value.habitId, intent.date)
                    }

                    _state.update {
                        it.copy(selectedDates = repository.getHabitDates(state.value.habitId))
                    }
                }

                DetailIntent.OnNavigateBack -> {
                    _sideEffect.emit(DetailSideEffect.RouteBack)
                }
            }
        }
    }

    private fun calculateCalendarData(targetDate: KtLocalDate): CalendarDataUi {
        val targetFirstDay = KtLocalDate(targetDate.year, targetDate.month, 1)
        val targetDaysInMonth = targetDate.toJavaLocalDate().lengthOfMonth()
        val targetFirstDayOfWeek = targetFirstDay.dayOfWeek.value
        val offset = targetFirstDayOfWeek - 1
        return CalendarDataUi(targetFirstDay, targetDaysInMonth, targetFirstDayOfWeek, offset)
    }
}
