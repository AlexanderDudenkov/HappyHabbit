package com.dudencov.happyhabit.presentation.detail

sealed interface DetailIntent {
    data class SetItemId(val id: String) : DetailIntent
    data class UpdateText(val newText: String) : DetailIntent
    data object OnClicked : DetailIntent
}