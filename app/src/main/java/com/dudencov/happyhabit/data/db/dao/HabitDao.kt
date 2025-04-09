package com.dudencov.happyhabit.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dudencov.happyhabit.data.db.entities.HabitEntity
import com.dudencov.happyhabit.data.db.entities.HabitWithSelectedDaysDto
import com.dudencov.happyhabit.data.db.entities.SelectedDateEntity
import kotlinx.datetime.LocalDate as KtLocalDate

@Dao
interface HabitDao {

    @Transaction
    @Query("SELECT * FROM Habit")
    suspend fun getAllHabitsWithDates(): List<HabitWithSelectedDaysDto>

    @Query("SELECT * FROM Habit")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    suspend fun getHabit(habitId: Int): HabitEntity?

    @Transaction
    @Query("SELECT * FROM Habit WHERE id = :habitId")
    suspend fun getHabitWithDays(habitId: Int): HabitWithSelectedDaysDto

    @Query("SELECT * FROM SelectedDate WHERE habitId = :habitId")
    suspend fun getHabitDates(habitId: Int): List<SelectedDateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedDate(day: SelectedDateEntity)

    @Query("UPDATE habit SET name = :name WHERE id =:id")
    suspend fun updateHabitNameById(id: Int, name: String)

    @Transaction
    @Query("DELETE FROM Habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Int)

    @Transaction
    @Query("DELETE FROM SelectedDate WHERE habitId = :habitId AND date = :date")
    suspend fun deleteDate(habitId: Int, date: KtLocalDate)

    @Query("SELECT EXISTS(SELECT 1 FROM Habit WHERE LOWER(name) = LOWER(:habitName))")
    suspend fun isHabitExist(habitName: String): Boolean
}
