package com.dudencov.happyhabit.feature.weekly

sealed interface WeeklyProgressIntent {
    data object OnCreate : WeeklyProgressIntent
    data object OnNavigateBack : WeeklyProgressIntent
}