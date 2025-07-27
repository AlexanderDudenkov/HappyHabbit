package com.dudencov.happyhabit.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dudencov.happyhabit.data.db.entities.ReminderTimeEntity
import kotlinx.datetime.LocalTime

@Dao
interface ReminderTimeDao {

    @Query("SELECT * FROM remindertime")
    suspend fun getAll(): List<ReminderTimeEntity>

    @Query("SELECT * FROM remindertime WHERE id = :id")
    suspend fun getReminder(id: Int): ReminderTimeEntity?

    @Query("SELECT is_reminder_on FROM ReminderTime WHERE id = :id")
    suspend fun getIsReminderOn(id: Int): Boolean?

    @Query("SELECT EXISTS(SELECT 1 FROM remindertime)")
    suspend fun isExist(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReminderTimeEntity)

    @Query("UPDATE remindertime SET reminder_time = :reminderTime WHERE id = :id")
    suspend fun updateReminderTimeById(id: Int, reminderTime: LocalTime?)

    @Query("UPDATE remindertime SET is_reminder_on = :isOn WHERE id = :id")
    suspend fun updateIsReminderOnById(id: Int, isOn: Boolean)
}
