package com.octopus.edu.kotlin.core.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class CoreModule {
    companion object {
        @Provides
        fun providesDispatcherProvider(): DispatcherProvider = CoroutineDispatcherProvider()
    }
}
