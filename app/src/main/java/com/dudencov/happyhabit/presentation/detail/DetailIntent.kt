package com.dudencov.happyhabit.presentation.detail

import java.time.LocalDate

sealed interface DetailIntent {
    data class SetHabitId(val id: String) : DetailIntent
    data class OnScreenSwiped(val direction: SwipeDirection) : DetailIntent
    data class OnDateSelected(val date: LocalDate) : DetailIntent
    data object OnNavigateBack : DetailIntent
}