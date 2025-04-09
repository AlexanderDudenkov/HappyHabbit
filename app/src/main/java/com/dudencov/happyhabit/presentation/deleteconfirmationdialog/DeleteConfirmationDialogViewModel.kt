package com.dudencov.happyhabit.presentation.deleteconfirmationdialog

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
import javax.inject.Inject

@HiltViewModel
class DeleteConfirmationDialogViewModel @Inject constructor(
    private val repository: HabitRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DeleteConfirmationDialogState())
    val state: StateFlow<DeleteConfirmationDialogState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DeleteConfirmationDialogSideEffect>()
    val sideEffect: SharedFlow<DeleteConfirmationDialogSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: DeleteConfirmationDialogIntent) {
        when (intent) {
            is DeleteConfirmationDialogIntent.OnSetHabitId -> {
                _state.update {
                    it.copy(habitId = intent.habitId)
                }
            }

            is DeleteConfirmationDialogIntent.OnConfirm -> {
                viewModelScope.launch {
                    repository.deleteHabit(state.value.habitId)
                    _sideEffect.emit(DeleteConfirmationDialogSideEffect.OnDismiss)
                }
            }

            is DeleteConfirmationDialogIntent.OnDismiss -> {
                viewModelScope.launch {
                    _sideEffect.emit(DeleteConfirmationDialogSideEffect.OnDismiss)
                }
            }
        }
    }
} 