package com.dudencov.happyhabit.presentation.weekly

sealed class WeeklySideEffect {

    data object RouteBack : WeeklySideEffect()
}