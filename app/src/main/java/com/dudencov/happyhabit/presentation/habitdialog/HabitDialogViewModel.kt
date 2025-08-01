package com.dudencov.happyhabit.presentation.habitdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.usecases.HabitValidationUseCase
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
    private val repository: HabitRepository,
    private val habitValidationUseCase: HabitValidationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HabitDialogState())
    val state: StateFlow<HabitDialogState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HabitDialogSideEffect>()
    val sideEffect: SharedFlow<HabitDialogSideEffect> = _sideEffect.asSharedFlow()

    private var initialHabitName = ""
    private var currentHabitId = -1

    fun onIntent(intent: HabitDialogIntent) {
        when (intent) {
            is HabitDialogIntent.OnChangeTitle -> {
                _state.update {
                    it.copy(title = intent.title)
                }
            }

            is HabitDialogIntent.OnSetHabitToTextField -> {
                if (currentHabitId == intent.habitId) return
                currentHabitId = intent.habitId

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
                val trimmedName = intent.text.trimStart()
                _state.update {
                    it.copy(
                        habitUi = it.habitUi.copy(name = trimmedName),
                        saveEnabled = habitValidationUseCase(trimmedName, initialHabitName),
                        errorResId = null
                    )
                }
            }

            is HabitDialogIntent.OnSave -> {
                viewModelScope.launch {
                    val trimmedName = state.value.habitUi.name.trim()
                    
                    if (initialHabitName.isEmpty()) {
                        if (repository.isHabitExist(trimmedName)) {
                            _state.update {
                                it.copy(errorResId = R.string.habit_dialog_error_duplicate_name)
                            }
                            return@launch
                        }
                        repository.createHabit(habitName = trimmedName)
                    } else {
                        repository.updateHabitName(
                            habitId = state.value.habitUi.id,
                            habitName = trimmedName
                        )
                    }
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