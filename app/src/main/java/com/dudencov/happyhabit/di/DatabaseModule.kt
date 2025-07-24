package com.dudencov.happyhabit.di

import android.content.Context
import androidx.room.Room
import com.dudencov.happyhabit.data.db.AppDatabase
import com.dudencov.happyhabit.data.db.DatabaseMigrations.MIGRATION_3_4
import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_3_4)
            .build()
    }

    @Provides
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    fun provideReminderTimeDao(database: AppDatabase): ReminderTimeDao {
        return database.reminderTimeDao()
    }
}
