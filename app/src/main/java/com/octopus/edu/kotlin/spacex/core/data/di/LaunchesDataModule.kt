package com.octopus.edu.kotlin.spacex.core.data.di

import com.octopus.edu.kotlin.spacex.core.data.LaunchRepository
import com.octopus.edu.kotlin.spacex.core.data.LaunchRepositoryImpl
import com.octopus.edu.kotlin.spacex.core.data.LaunchesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LaunchesDataModule {
    @Binds
    abstract fun launchesRepository(repository: LaunchRepositoryImpl): LaunchRepository

    companion object {
        @Provides
        @Singleton
        fun launchesApi(retrofit: Retrofit): LaunchesApi = retrofit.create(LaunchesApi::class.java)
    }
}
