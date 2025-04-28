package com.octopus.edu.kotlin.core.data.launches.di

import com.octopus.edu.kotlin.core.data.launches.RocketApi
import dagger.hilt.components.SingletonComponent

@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
abstract class RocketDataModule {
    companion object {
        @dagger.Provides
        @javax.inject.Singleton
        fun rocketApi(retrofit: retrofit2.Retrofit): RocketApi = retrofit.create(RocketApi::class.java)
    }
}
