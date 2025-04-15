package com.octopus.edu.kotlin.spacex.core.data

import com.octopus.edu.kotlin.spacex.core.model.Launch
import com.octopus.edu.kotlin.spacex.core.model.LaunchDetails
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation

interface LaunchRepository {

    suspend fun getAllLaunches(): ResponseOperation<List<Launch>>

    suspend fun getLaunchDetails(flightNumber: Int): ResponseOperation<LaunchDetails>
}