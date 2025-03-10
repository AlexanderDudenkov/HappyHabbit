package com.dudencov.happyhabit.presentation.detail

import java.time.LocalDate

sealed interface DetailIntent {
    data class SetHabitId(val id: Int) : DetailIntent
    data class OnScreenSwiped(val direction: SwipeDirection) : DetailIntent
    data class OnDateSelected(val date: LocalDate) : DetailIntent
    data object OnNavigateBack : DetailIntent
}