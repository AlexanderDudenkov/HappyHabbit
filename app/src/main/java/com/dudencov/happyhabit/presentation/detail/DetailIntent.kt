package com.dudencov.happyhabit.presentation.detail

import kotlinx.datetime.LocalDate as KtLocalDate

sealed interface DetailIntent {
    data class SetHabitId(val id: Int) : DetailIntent
    data class OnScreenSwiped(val direction: SwipeDirection) : DetailIntent
    data class OnDateSelected(val date: KtLocalDate) : DetailIntent
    data object OnNavigateBack : DetailIntent
}