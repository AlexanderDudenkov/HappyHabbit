package com.dudencov.happyhabit.domain.data

import com.dudencov.happyhabit.data.entities.Habit
import java.time.LocalDate

interface Repository {

    suspend fun saveHabit(habit: Habit): Habit

    suspend fun getHabit(id: String): Habit?

    suspend fun deleteHabit(id: String)

    suspend fun getAllHabits(): List<Habit>

    suspend fun saveDate(habitId: String, date: LocalDate)

    suspend fun getDates(habitId: String): Set<LocalDate>

    suspend fun deleteDate(habitId: String, date: LocalDate)
}
