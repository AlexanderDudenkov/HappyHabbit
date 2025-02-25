package com.dudencov.happyhabit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.data.RepositoryImpl
import com.dudencov.happyhabit.presentation.entities.HabitItemUi
import com.dudencov.happyhabit.presentation.entities.toHabitUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                HomeIntent.OnResume -> {
                    val habitItems = RepositoryImpl.getAllHabits().map {
                        HabitItemUi(habit = it.toHabitUi())
                    }

                    _state.update {
                        it.copy(
                            emptyStateVisible = habitItems.isEmpty(),
                            habitItems = habitItems,
                        )
                    }
                }

                is HomeIntent.OnHabitClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToDetails(intent.habitId))
                }

                HomeIntent.OnFabClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToDialog())
                }

                is HomeIntent.OnHabitDeleteClicked -> {
                    RepositoryImpl.deleteHabit(intent.id)
                    val habitItems = RepositoryImpl.getAllHabits().map {
                        HabitItemUi(habit = it.toHabitUi())
                    }

                    _state.update {
                        it.copy(
                            emptyStateVisible = it.habitItems.size == 1,
                            habitItems = habitItems,
                        )
                    }
                }

                is HomeIntent.OnHabitEditClicked -> {
                    _state.update { state ->
                        state.updateItemMenuExpandState(
                            habitId = intent.currentHabitId,
                            isExpanded = false
                        )
                    }
                    _sideEffect.emit(
                        HomeSideEffect.RouteToDialog(habitId = intent.currentHabitId)
                    )
                }

                HomeIntent.OnWeeklyProgressClicked -> {
                    _sideEffect.emit(HomeSideEffect.RouteToWeeklyProgress)
                }

                is HomeIntent.OnHabitItemMenuClicked -> {
                    _state.update { state ->
                        state.updateItemMenuExpandState(
                            habitId = intent.habitId,
                            isExpanded = intent.isExpended.not()
                        )
                    }
                }

                is HomeIntent.OnHabitItemMenuDismissed -> {
                    _state.update {
                        it.updateItemMenuExpandState(intent.habitId, false)
                    }
                }
            }
        }
    }

}