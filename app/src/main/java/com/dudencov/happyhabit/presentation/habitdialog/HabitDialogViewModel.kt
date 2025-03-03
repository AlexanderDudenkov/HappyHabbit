package com.dudencov.happyhabit.presentation.habitdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.domain.data.Repository
import com.dudencov.happyhabit.presentation.entities.toHabit
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
class HabitDialogViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(HabitDialogState())
    val state: StateFlow<HabitDialogState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HabitDialogSideEffect>()
    val sideEffect: SharedFlow<HabitDialogSideEffect> = _sideEffect.asSharedFlow()

    private var initialHabitName = ""

    fun onIntent(intent: HabitDialogIntent) {
        when (intent) {
            is HabitDialogIntent.OnChangeTitle -> {
                _state.update {
                    it.copy(title = intent.title)
                }
            }

            is HabitDialogIntent.OnSetHabitToTextField -> {
                viewModelScope.launch {
                    val habit =
                        repository.getHabit(intent.habitId)?.toHabitUi() ?: return@launch
                    initialHabitName = habit.name

                    _state.update {
                        it.copy(
                            habitUi = habit,
                            saveEnabled = false
                        )
                    }
                }
            }

            is HabitDialogIntent.OnTextChanged -> {
                _state.update {
                    it.copy(
                        habitUi = it.habitUi.copy(name = intent.text),
                        saveEnabled = intent.text.isNotEmpty() && intent.text != initialHabitName,
                    )
                }
            }

            is HabitDialogIntent.OnSave -> {
                viewModelScope.launch {
                    repository.saveHabit(state.value.habitUi.toHabit())
                    _sideEffect.emit(HabitDialogSideEffect.OnDismiss)
                }
            }

            is HabitDialogIntent.OnCancel -> {
                viewModelScope.launch {
                    _sideEffect.emit(HabitDialogSideEffect.OnDismiss)
                }
            }
        }
    }
}