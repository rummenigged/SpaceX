package com.octopus.edu.kotlin.core.data.launches

import com.octopus.edu.kotlin.spacex.core.model.LaunchDetails
import com.octopus.edu.kotlin.spacex.core.model.LaunchStatus
import com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse
import com.octopus.edu.kotlin.spacex.core.network.utils.asOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchRepositoryImpl
    @javax.inject.Inject
    constructor(
        private val launchApi: LaunchesApi,
        private val rocketApi: RocketApi,
    ) : LaunchRepository {
        override suspend fun getAllLaunches(): com.octopus.edu.kotlin.spacex.core.model.ResponseOperation<List<com.octopus.edu.kotlin.spacex.core.model.Launch>> =
            withContext(
                Dispatchers.IO,
            ) {
                launchApi
                    .getLaunches()
                    .map { output -> output.map { launch -> launch.toDomain() } }
                    .asOperation()
            }

        override suspend fun getLaunchDetails(
            flightNumber: Int,
        ): com.octopus.edu.kotlin.spacex.core.model.ResponseOperation<com.octopus.edu.kotlin.spacex.core.model.LaunchDetails> =
            withContext(Dispatchers.IO) {
                when (val launchDetailsResult = launchApi.getLaunchDetails(flightNumber)) {
                    is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.Success -> {
                        val launch = launchDetailsResult.data
                        when (
                            val rocketDetailsResult =
                                rocketApi.getRocketDetails(LaunchDTO.RocketDTO.id ?: "")
                        ) {
                            is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.Success -> {
                                val rocket = rocketDetailsResult.data
                                val result =
                                    com.octopus.edu.kotlin.spacex.core.model.LaunchDetails(
                                        missionName = LaunchDTO.name,
                                        flightNumber = LaunchDTO.flightNumber,
                                        date = LaunchDTO.date,
                                        siteName = LaunchDTO.Site.longName.orEmpty(),
                                        rocket = rocket.toDomain(),
                                        if (LaunchDTO.isLaunchSuccess == true) {
                                            com.octopus.edu.kotlin.spacex.core.model.LaunchStatus.Success
                                        } else {
                                            com.octopus.edu.kotlin.spacex.core.model.LaunchStatus.Failure(
                                                LaunchDTO.LaunchFailureDetails.reason.orEmpty(),
                                            )
                                        },
                                        patch = LaunchDTO.Links.patch,
                                    )
                                com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse
                                    .Success(
                                        result,
                                    ).asOperation()
                            }

                            is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.ApiError,
                            is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.NetworkError,
                            -> rocketDetailsResult.asOperation()
                        }
                    }

                    is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.ApiError,
                    is com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.NetworkError,
                    -> launchDetailsResult.asOperation()
                }
            }
    }
