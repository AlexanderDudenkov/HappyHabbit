package com.dudencov.happyhabit.presentation.home

import androidx.compose.ui.util.fastAny
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect

    fun onIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.OnHabitClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToDetails(intent.habitId))
                }

                HomeIntent.OnFabClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToDialog())
                }

                is HomeIntent.OnHabitDeleteClicked -> {
                    RepositoryImpl.deleteHabit(intent.id)
                    _state.update { state ->
                        state.copy(habits = state.habits.deleteHabit(intent.id))
                    }
                }

                is HomeIntent.OnHabitEditClicked -> {
                    _sideEffect.emit(
                        HomeSideEffect.RouteToDialog(habitId = intent.currentHabitId)
                    )
                }

                HomeIntent.OnWeeklyProgressClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToWeeklyProgress)
                }

                is HomeIntent.OnHabitSaved -> {
                    val habit = RepositoryImpl.getHabit(intent.newHabitId) ?: return@launch
                    _state.update {
                        it.copy(habits = it.createOrUpdateHabit(habit))
                    }
                }
            }
        }
    }

    private fun HomeState.createOrUpdateHabit(new: Habit): List<Habit> {
        return if (habits.isHabitExist(new)) {
            habits.updateHabit(newHabit = new)
        } else {
            habits.createHabit(newHabit = new)
        }
    }

    private fun List<Habit>.isHabitExist(habit: Habit): Boolean {
        return fastAny { it.id == habit.id }
    }

    private fun List<Habit>.createHabit(newHabit: Habit): List<Habit> {
        return this + listOf(newHabit)
    }

    private fun List<Habit>.updateHabit(newHabit: Habit): List<Habit> {
        return this.map {
            if (it.id == newHabit.id) newHabit else it
        }
    }

    private fun List<Habit>.deleteHabit(habitId: String): List<Habit> {
        return this.filterNot { it.id == habitId }
    }

}