package com.dudencov.happyhabit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    private val _sideEffect = MutableSharedFlow<DetailSideEffect>()
    val sideEffect: SharedFlow<DetailSideEffect> = _sideEffect

    fun onIntent(intent: DetailIntent) {
        viewModelScope.launch {
            when (intent) {
                is DetailIntent.SetItemId -> {
                    _state.value = _state.value.copy(itemId = intent.id)
                }

                is DetailIntent.UpdateText -> {
                    //_state.value = _state.value.copy(text = intent.newText)
                }

                DetailIntent.OnClicked -> _sideEffect.emit(DetailSideEffect.RouteHome)
            }
        }
    }
}
