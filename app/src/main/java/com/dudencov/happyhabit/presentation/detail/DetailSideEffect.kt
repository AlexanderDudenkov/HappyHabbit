package com.dudencov.happyhabit.presentation.detail

sealed class DetailSideEffect {
    data object RouteBack : DetailSideEffect()
}