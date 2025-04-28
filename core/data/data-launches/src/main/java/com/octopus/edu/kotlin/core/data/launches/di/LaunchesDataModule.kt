package com.octopus.edu.kotlin.core.data.launches.di

import com.octopus.edu.kotlin.core.data.launches.LaunchRepository
import com.octopus.edu.kotlin.core.data.launches.LaunchRepositoryImpl
import com.octopus.edu.kotlin.core.data.launches.LaunchesApi
import dagger.hilt.components.SingletonComponent

@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
abstract class LaunchesDataModule {
    @dagger.Binds
    abstract fun launchesRepository(repository: LaunchRepositoryImpl): LaunchRepository

    companion object {
        @dagger.Provides
        @javax.inject.Singleton
        fun launchesApi(retrofit: retrofit2.Retrofit): LaunchesApi = retrofit.create(LaunchesApi::class.java)
    }
}
