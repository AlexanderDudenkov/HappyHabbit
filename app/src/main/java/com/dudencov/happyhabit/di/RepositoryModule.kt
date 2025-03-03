package com.dudencov.happyhabit.di

import com.dudencov.happyhabit.domain.data.Repository
import com.dudencov.happyhabit.data.RepositoryImpl
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
    fun provideRepository(): Repository {
        return RepositoryImpl
    }
}