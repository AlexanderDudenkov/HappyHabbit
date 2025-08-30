package com.dudencov.happyhabit.di

import com.dudencov.happyhabit.domain.data.NotificationScheduler
import com.dudencov.happyhabit.notification.HabitNotificationScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(
        impl: HabitNotificationScheduler
    ): NotificationScheduler
}