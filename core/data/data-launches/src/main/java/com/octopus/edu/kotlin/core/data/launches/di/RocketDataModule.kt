package com.octopus.edu.kotlin.core.data.launches.di

import com.octopus.edu.kotlin.core.data.launches.RocketApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RocketDataModule {
    companion object {
        @Provides
        @Singleton
        fun rocketApi(retrofit: retrofit2.Retrofit): RocketApi = retrofit.create(RocketApi::class.java)
    }
}
