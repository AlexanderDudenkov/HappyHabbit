package com.dudencov.happyhabit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import com.dudencov.happyhabit.data.db.entities.HabitEntity
import com.dudencov.happyhabit.data.db.entities.ReminderTimeEntity
import com.dudencov.happyhabit.data.db.entities.SelectedDateEntity

@Database(
    entities = [HabitEntity::class,
        SelectedDateEntity::class,
        ReminderTimeEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun reminderTimeDao(): ReminderTimeDao
}
