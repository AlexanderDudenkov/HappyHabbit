package com.dudencov.happyhabit.di

import com.dudencov.happyhabit.data.HabitRepositoryImpl
import com.dudencov.happyhabit.domain.data.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideRepository(habitRepository: HabitRepositoryImpl): HabitRepository
}