package com.dudencov.happyhabit.data.db

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromLocalDateSet(dates: Set<LocalDate>): String {
        return dates.joinToString(",") { it.format(formatter) }
    }

    @TypeConverter
    fun toLocalDateSet(datesString: String): Set<LocalDate> {
        return datesString.split(",").map { LocalDate.parse(it, formatter) }.toSet()
    }
}
