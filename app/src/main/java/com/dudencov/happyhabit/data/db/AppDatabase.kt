package com.dudencov.happyhabit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.entities.HabitEntity
import com.dudencov.happyhabit.data.db.entities.SelectedDateEntity

@Database(entities = [HabitEntity::class, SelectedDateEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
