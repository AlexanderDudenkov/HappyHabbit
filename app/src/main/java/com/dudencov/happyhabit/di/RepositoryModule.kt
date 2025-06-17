package com.dudencov.happyhabit.di

import android.content.Context
import com.dudencov.happyhabit.data.HabitRepositoryImpl
import com.dudencov.happyhabit.data.NotificationsRepositoryImpl
import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(habitDao: HabitDao): HabitRepository {
        return HabitRepositoryImpl(habitDao)
    }

    @Singleton
    @Provides
    fun provideNotificationsRepository(
        @ApplicationContext context: Context,
        reminderTimeDao: ReminderTimeDao,
    ): NotificationsRepository {
        return NotificationsRepositoryImpl(reminderTimeDao, context)
    }
}