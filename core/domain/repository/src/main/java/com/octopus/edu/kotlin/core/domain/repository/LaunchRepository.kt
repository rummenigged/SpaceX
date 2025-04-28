package com.octopus.edu.kotlin.core.domain.repository

import com.octopus.edu.kotlin.core.domain.common.ResponseOperation

interface LaunchRepository {
    suspend fun getAllLaunches(): ResponseOperation<List<com.octopus.edu.kotlin.core.domain.models.launch.Launch>>

    suspend fun getLaunchDetails(flightNumber: Int): ResponseOperation<com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails>
}
