package com.dudencov.happyhabit.presentation.detail

sealed class DetailSideEffect {
    data class ShowMessage(val message: String) : DetailSideEffect()
    data object RouteHome : DetailSideEffect()
}