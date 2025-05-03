package com.octopus.edu.kotlin.core.domain.repository

import com.octopus.edu.kotlin.core.domain.common.ResponseOperation
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails

interface LaunchRepository {
    suspend fun getAllLaunches(): ResponseOperation<List<Launch>>

    suspend fun getLaunchDetails(flightNumber: Int): ResponseOperation<LaunchDetails>

    suspend fun getPastLaunches(): ResponseOperation<List<Launch>>

    suspend fun getUpcomingLaunches(): ResponseOperation<List<Launch>>
}
