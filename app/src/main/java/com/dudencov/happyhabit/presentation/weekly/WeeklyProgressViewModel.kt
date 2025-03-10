package com.dudencov.happyhabit.presentation.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.presentation.utils.getCurrentWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeeklyProgressViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeeklyProgressState())
    val state: StateFlow<WeeklyProgressState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<WeeklySideEffect>()
    val sideEffect: SharedFlow<WeeklySideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: WeeklyProgressIntent) {
        viewModelScope.launch {
            when (intent) {
                WeeklyProgressIntent.OnCreate -> {
                    _state.update {
                        it.copy(habits = createWeeklyHabitsUi())
                    }
                }

                WeeklyProgressIntent.OnNavigateBack -> {
                    _sideEffect.emit(WeeklySideEffect.RouteBack)
                }
            }
        }
    }

    private suspend fun createWeeklyHabitsUi(): List<WeeklyHabitUi> {
        val currentWeek = getCurrentWeek()

        return repository.getAllHabitsWithDates(currentWeek).map {
            WeeklyHabitUi(
                id = it.key.id,
                name = it.key.name,
                days = createWeeklyDayUi(it.value)
            )
        }
    }

    private fun createWeeklyDayUi(selectedDays: Set<LocalDate>) =
        DayOfWeek.entries.map { dayOfWeek ->
            WeeklyDayUi(
                isSelected = selectedDays.any {
                    it.dayOfWeek == dayOfWeek
                }
            )
        }

}