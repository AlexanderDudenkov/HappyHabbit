package com.dudencov.happyhabit.data

import com.dudencov.happyhabit.presentation.home.Habit
import kotlin.random.Random

interface Repository {

    suspend fun saveHabit(habit: Habit): Habit

    suspend fun getHabit(id: String): Habit?

    suspend fun deleteHabit(id:String)
}

object RepositoryImpl : Repository {

    private val map = HashMap<String, Habit>()

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
}