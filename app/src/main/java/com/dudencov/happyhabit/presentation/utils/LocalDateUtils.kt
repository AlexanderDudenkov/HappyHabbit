package com.dudencov.happyhabit.presentation.utils

import java.time.DayOfWeek
import java.time.LocalDate

fun getCurrentWeek(): ClosedRange<LocalDate> {
    val today = LocalDate.now()
    val firstDayOfWeek = today.with(DayOfWeek.MONDAY)
    val lastDayOfWeek = today.with(DayOfWeek.SUNDAY)
    return firstDayOfWeek..lastDayOfWeek
}