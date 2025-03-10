package com.dudencov.happyhabit.data

import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.entities.HabitEntity
import com.dudencov.happyhabit.data.db.entities.SelectedDateEntity
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.entities.Habit
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override suspend fun createHabit(habitName: String) {
        habitDao.insertHabit(HabitEntity(name = habitName))
    }

    override suspend fun getHabit(id: Int): Habit? {
        return habitDao.getHabit(id)?.let {
            Habit(
                id = it.id,
                name = it.name,
            )
        }
    }

    override suspend fun updateHabitName(habitId: Int, habitName: String) {
        habitDao.updateHabitNameById(
            id = habitId,
            name = habitName
        )
    }

    override suspend fun getAllHabits(): List<Habit> {
        return habitDao.getAllHabits().map {
            Habit(
                id = it.id,
                name = it.name
            )
        }
    }

    override suspend fun deleteHabit(id: Int) {
        habitDao.deleteHabitById(id)
    }

    override suspend fun getAllHabitsWithDates(period: ClosedRange<LocalDate>): Map<Habit, Set<LocalDate>> {
        return habitDao.getAllHabitsWithDates().associate { habitWithDates ->
            Pair(
                first = Habit(
                    id = habitWithDates.habit.id,
                    name = habitWithDates.habit.name
                ),
                second = habitWithDates.dates.map { it.date }.toSet()
            )
        }
    }

    override suspend fun createCurrentDate(habitId: Int, date: LocalDate) {
        habitDao.insertSelectedDate(
            SelectedDateEntity(
                habitId = habitId,
                date = date
            )
        )
    }

    override suspend fun getHabitDates(
        habitId: Int,
        period: ClosedRange<LocalDate>
    ): Set<LocalDate> {
        return habitDao.getHabitDates(habitId = habitId).map { it.date }.toSet()
    }

    override suspend fun deleteDate(habitId: Int, date: LocalDate) {
        habitDao.deleteDate(habitId = habitId, date = date)
    }
}