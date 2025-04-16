package com.octopus.edu.kotlin.spacex.core.data.di

import com.octopus.edu.kotlin.spacex.core.data.RocketApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RocketDataModule {
    companion object {
        @Provides
        @Singleton
        fun rocketApi(retrofit: Retrofit): RocketApi = retrofit.create(RocketApi::class.java)
    }
}
