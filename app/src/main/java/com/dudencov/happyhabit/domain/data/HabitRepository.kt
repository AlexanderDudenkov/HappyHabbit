package com.dudencov.happyhabit.domain.data

import com.dudencov.happyhabit.domain.entities.Habit
import com.dudencov.happyhabit.presentation.utils.max
import com.dudencov.happyhabit.presentation.utils.min
import kotlinx.datetime.LocalDate as KtLocalDate

interface HabitRepository {

    suspend fun createHabit(habitName: String)

    suspend fun updateHabitName(habitId: Int, habitName: String)

    suspend fun getHabit(id: Int): Habit?

    suspend fun deleteHabit(id: Int)

    suspend fun getAllHabitsWithDates(
        period: ClosedRange<KtLocalDate> = KtLocalDate.min..KtLocalDate.max
    ): Map<Habit, Set<KtLocalDate>>

    suspend fun getAllHabits(): List<Habit>

    suspend fun createCurrentDate(habitId: Int, date: KtLocalDate)

    suspend fun getHabitDates(
        habitId: Int,
        period: ClosedRange<KtLocalDate> = KtLocalDate.min..KtLocalDate.max
    ): Set<KtLocalDate>

    suspend fun deleteDate(habitId: Int, date: KtLocalDate)
    
    suspend fun isHabitExist(habitName: String): Boolean
}