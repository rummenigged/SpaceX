package com.octopus.edu.kotlin.core.data.launches.di

import com.octopus.edu.kotlin.core.data.launches.LaunchRepositoryImpl
import com.octopus.edu.kotlin.core.data.launches.LaunchesApi
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LaunchesDataModule {
    @Binds
    abstract fun launchesRepository(repository: LaunchRepositoryImpl): LaunchRepository

    companion object {
        @Provides
        @Singleton
        fun launchesApi(retrofit: retrofit2.Retrofit): LaunchesApi = retrofit.create(LaunchesApi::class.java)
    }
}
