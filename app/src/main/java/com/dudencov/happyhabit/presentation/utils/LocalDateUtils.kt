package com.dudencov.happyhabit.presentation.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.LocalDate as KtLocalDate

fun getCurrentWeek(): ClosedRange<KtLocalDate> {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val todayDayOfWeek = today.dayOfWeek
    val daysFromMonday = todayDayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
    val firstDayOfWeek = today.minus(daysFromMonday, DateTimeUnit.DAY)
    val lastDayOfWeek = firstDayOfWeek.plus(6, DateTimeUnit.DAY)
    return firstDayOfWeek..lastDayOfWeek
}