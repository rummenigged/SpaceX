package com.octopus.edu.kotlin.core.data.launches.di

import com.octopus.edu.kotlin.core.data.common.DataCache
import com.octopus.edu.kotlin.core.data.common.DataConstants
import com.octopus.edu.kotlin.core.data.common.LaunchDetailsCache
import com.octopus.edu.kotlin.core.data.launches.LaunchRepositoryImpl
import com.octopus.edu.kotlin.core.data.launches.api.LaunchesApi
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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

        @Provides
        @Named("launch_details_cache_size")
        fun launchDetailsCacheSize(): Int = DataConstants.LAUNCH_DETAILS_CACHE_SIZE

        @Provides
        @Singleton
        fun launchDetailsCache(
            @Named("launch_details_cache_size") cacheSize: Int
        ): DataCache<Int, LaunchDetails> = LaunchDetailsCache(cacheSize)
    }
}
