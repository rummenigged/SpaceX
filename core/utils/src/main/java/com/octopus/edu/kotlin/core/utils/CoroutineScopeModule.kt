package com.octopus.edu.kotlin.core.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@InstallIn(SingletonComponent::class)
@Module
class CoroutineScopeModule {
    @ApplicationScope
    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}
