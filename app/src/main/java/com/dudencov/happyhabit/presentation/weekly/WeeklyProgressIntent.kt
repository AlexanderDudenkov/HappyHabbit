package com.dudencov.happyhabit.presentation.weekly

sealed interface WeeklyProgressIntent {
    data object OnCreate : WeeklyProgressIntent
    data object OnNavigateBack : WeeklyProgressIntent
}