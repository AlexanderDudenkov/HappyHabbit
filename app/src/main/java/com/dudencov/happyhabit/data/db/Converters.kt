package com.dudencov.happyhabit.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate as KtLocalDate
import kotlinx.datetime.LocalTime

class Converters {

    @TypeConverter
    fun fromLocalDate(date: KtLocalDate): String {
        return date.toString() // Outputs ISO-8601 format, e.g., "2023-01-01"
    }

    @TypeConverter
    fun toLocalDate(dateString: String): KtLocalDate {
        return KtLocalDate.parse(dateString) // Parses ISO-8601 format
    }

    @TypeConverter
    fun fromLocalDateSet(dates: Set<KtLocalDate>): String {
        return dates.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun toLocalDateSet(datesString: String): Set<KtLocalDate> {
        return if (datesString.isEmpty()) emptySet()
        else datesString.split(",").map { KtLocalDate.parse(it) }.toSet()
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.toString()
    }

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it) }
    }
}
