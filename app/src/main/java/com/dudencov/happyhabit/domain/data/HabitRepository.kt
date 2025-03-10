package com.dudencov.happyhabit.domain.data

import com.dudencov.happyhabit.domain.entities.Habit
import java.time.LocalDate

interface HabitRepository {

    suspend fun createHabit(habitName: String)

    suspend fun updateHabitName(habitId: Int, habitName: String)

    suspend fun getHabit(id: Int): Habit?

    suspend fun deleteHabit(id: Int)

    suspend fun getAllHabitsWithDates(period: ClosedRange<LocalDate> = LocalDate.MIN..LocalDate.MAX): Map<Habit, Set<LocalDate>>

    suspend fun getAllHabits(): List<Habit>

    suspend fun createCurrentDate(habitId: Int, date: LocalDate)

    suspend fun getHabitDates(
        habitId: Int,
        period: ClosedRange<LocalDate> = LocalDate.MIN..LocalDate.MAX
    ): Set<LocalDate>

    suspend fun deleteDate(habitId: Int, date: LocalDate)
}
