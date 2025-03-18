package com.dudencov.happyhabit.presentation.detail

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.LocalDate as KtLocalDate
import java.time.format.TextStyle
import java.util.Locale

data class DetailState(
    val habitId: Int = 0,
    val currentDate: KtLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val selectedDates: Set<KtLocalDate> = emptySet(),
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