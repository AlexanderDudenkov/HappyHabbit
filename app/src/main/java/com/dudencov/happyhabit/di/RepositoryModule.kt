package com.dudencov.happyhabit.di

import com.dudencov.happyhabit.data.HabitRepositoryImpl
import com.dudencov.happyhabit.data.NotificationsRepositoryImpl
import com.dudencov.happyhabit.data.db.dao.HabitDao
import com.dudencov.happyhabit.data.db.dao.ReminderTimeDao
import com.dudencov.happyhabit.data.notifications.HabitNotificationScheduler
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.data.NotificationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        reminderTimeDao: ReminderTimeDao,
        notificationScheduler: HabitNotificationScheduler
    ): NotificationsRepository {
        return NotificationsRepositoryImpl(reminderTimeDao, notificationScheduler)
    }
}