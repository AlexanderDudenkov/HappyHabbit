package com.dudencov.happyhabit.presentation.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDate as KtLocalDate

val KtLocalDate.Companion.min: KtLocalDate
    get() = KtLocalDate(1970, 1, 1)

val KtLocalDate.Companion.max: KtLocalDate
    get() = KtLocalDate(9999, 12, 31)

fun KtLocalDate.isCurrentDay(): Boolean {
    return this == Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}