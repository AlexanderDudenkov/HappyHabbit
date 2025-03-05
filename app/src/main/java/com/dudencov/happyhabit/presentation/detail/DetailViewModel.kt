package com.dudencov.happyhabit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.domain.data.Repository
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
class DetailViewModel @Inject constructor(
    private val repository: Repository
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
                            selectedDates = repository.getDates(habitId = intent.id)
                        )
                    }
                }

                is DetailIntent.OnScreenSwiped -> {
                    _state.update {
                        val res = if (intent.direction == SwipeDirection.LEFT) {
                            it.currentDate.plusMonths(1)
                        } else {
                            it.currentDate.minusMonths(1)
                        }

                        it.copy(currentDate = res, swipeDirection = intent.direction)
                    }
                }

                is DetailIntent.OnDateSelected -> {
                    val isAlreadySelected = state.value.selectedDates.contains(intent.date)

                    if (isAlreadySelected) {
                        repository.deleteDate(state.value.habitId, intent.date)
                    } else {
                        repository.saveDate(state.value.habitId, intent.date)
                    }

                    _state.update {
                        it.copy(selectedDates = repository.getDates(state.value.habitId))
                    }
                }

                DetailIntent.OnNavigateBack -> {
                    _sideEffect.emit(DetailSideEffect.RouteBack)
                }
            }
        }
    }
}
