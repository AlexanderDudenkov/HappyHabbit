package com.dudencov.happyhabit.presentation.detail

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class DetailState(
    val habitId: String = "",
    val currentDate: LocalDate = LocalDate.now(),
    val selectedDates: Set<LocalDate> = emptySet(),
    val swipeDirection: SwipeDirection = SwipeDirection.NONE
) {
    fun createTitle(): String {
        return "${
            currentDate.month.getDisplayName(
                TextStyle.FULL_STANDALONE,
                Locale.getDefault()
            ).replaceFirstChar { char -> char.uppercase() }
        } ${currentDate.year}"
    }
}