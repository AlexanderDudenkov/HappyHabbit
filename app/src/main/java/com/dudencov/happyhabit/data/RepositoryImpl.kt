package com.dudencov.happyhabit.data

import com.dudencov.happyhabit.data.entities.Habit
import com.dudencov.happyhabit.domain.data.Repository
import java.time.LocalDate
import kotlin.random.Random

object RepositoryImpl : Repository {

    private val map = LinkedHashMap<String, Habit>()
    private val dates = HashMap<String, Set<Long>>()

    override suspend fun getAllHabits(): List<Habit> {
        return map.values.toList()
    }

    override suspend fun saveHabit(habit: Habit): Habit {
        val id = habit.id.takeIf { it.isNotEmpty() } ?: Random.nextInt().toString()
        val withId = habit.copy(id = id)
        map[id] = withId
        return withId
    }

    override suspend fun getHabit(id: String): Habit? {
        return map[id]
    }

    override suspend fun deleteHabit(id: String) {
        map.remove(id)
    }

    override suspend fun saveDate(habitId: String, date: LocalDate) {
        val ed = date.toEpochDay()
        val dates1 = dates[habitId]

        if (dates1 != null) {
            dates[habitId] = dates1.toMutableSet().plus(ed)
        } else {
            dates[habitId] = setOf(ed)
        }

    }

    override suspend fun getDates(habitId: String): Set<LocalDate> {
        val ed: Set<Long> = dates[habitId] ?: emptySet()

        return ed.map { LocalDate.ofEpochDay(it) }.toSet()
    }

    override suspend fun deleteDate(habitId: String, date: LocalDate) {
        dates[habitId]?.let {
            dates[habitId] = it.minus(date.toEpochDay())
        }
    }
}