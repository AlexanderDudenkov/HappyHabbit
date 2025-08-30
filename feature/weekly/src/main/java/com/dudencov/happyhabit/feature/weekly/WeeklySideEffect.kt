package com.dudencov.happyhabit.feature.weekly

sealed class WeeklySideEffect {

    data object RouteBack : WeeklySideEffect()
}