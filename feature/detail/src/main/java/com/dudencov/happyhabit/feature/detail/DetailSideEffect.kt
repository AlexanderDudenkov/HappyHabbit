package com.dudencov.happyhabit.feature.detail

sealed class DetailSideEffect {
    data object RouteBack : DetailSideEffect()
}