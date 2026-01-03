package com.dudencov.happyhabit.presentation.detail

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn

data class CalendarDataUi(
    val targetFirstDay: LocalDate = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()).let { today ->
        LocalDate(today.year, today.month, 1)
    },
    val targetDaysInMonth: Int = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()).toJavaLocalDate().lengthOfMonth(),
    val targetFirstDayOfWeek: Int = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()).let { today ->
        LocalDate(today.year, today.month, 1).dayOfWeek.isoDayNumber
    },
    val offset: Int = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()).let { today ->
        LocalDate(today.year, today.month, 1).dayOfWeek.isoDayNumber - 1
    }
)