package com.dudencov.happyhabit.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SettingsSideEffect>()
    val sideEffect: SharedFlow<SettingsSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.OnNavigateBack -> {
                    _sideEffect.emit(SettingsSideEffect.RouteBack)
                }

                is SettingsIntent.OnNotificationsClicked -> {
                    _sideEffect.emit(SettingsSideEffect.RouteToNotifications)
                }
            }
        }
    }
} 