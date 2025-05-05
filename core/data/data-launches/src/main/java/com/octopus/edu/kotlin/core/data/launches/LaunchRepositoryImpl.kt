package com.octopus.edu.kotlin.core.data.launches

import com.octopus.edu.kotlin.core.data.launches.api.LaunchesApi
import com.octopus.edu.kotlin.core.data.launches.api.RocketApi
import com.octopus.edu.kotlin.core.data.launches.dto.toDomain
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import com.octopus.edu.kotlin.core.network.utils.NetworkResponse
import com.octopus.edu.kotlin.core.network.utils.asOperation
import com.octopus.edu.kotlin.core.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchRepositoryImpl
    @Inject
    constructor(
        private val launchApi: LaunchesApi,
        private val rocketApi: RocketApi,
        private val dispatcherProvider: DispatcherProvider
    ) : LaunchRepository {
        override suspend fun getAllLaunches(): ResponseOperation<List<Launch>> =
            withContext(
                dispatcherProvider.io,
            ) {
                launchApi
                    .getLaunches()
                    .map { output -> output.map { launch -> launch.toDomain() } }
                    .asOperation()
            }

        override suspend fun getLaunchDetails(flightNumber: Int): ResponseOperation<LaunchDetails> =
            withContext(dispatcherProvider.io) {
                when (val launchDetailsResult = launchApi.getLaunchDetails(flightNumber)) {
                    is NetworkResponse.Success -> {
                        val launch = launchDetailsResult.data
                        when (
                            val rocketDetailsResult =
                                rocketApi.getRocketDetails(launch.rocket.id ?: "")
                        ) {
                            is NetworkResponse.Success -> {
                                val rocket = rocketDetailsResult.data
                                val result =
                                    LaunchDetails(
                                        missionName = launch.name,
                                        flightNumber = launch.flightNumber,
                                        date = launch.date,
                                        siteName = launch.site?.longName.orEmpty(),
                                        rocket = rocket.toDomain(),
                                        if (launch.isLaunchSuccess == true) {
                                            LaunchStatus.Success
                                        } else {
                                            LaunchStatus.Failure(
                                                launch.launchFailureDetails?.reason.orEmpty(),
                                            )
                                        },
                                        patch = launch.links.patch,
                                    )
                                NetworkResponse
                                    .Success(
                                        result,
                                    ).asOperation()
                            }

                            is NetworkResponse.ApiError,
                            is NetworkResponse.NetworkError,
                            -> rocketDetailsResult.asOperation()
                        }
                    }

                    is NetworkResponse.ApiError,
                    is NetworkResponse.NetworkError,
                    -> launchDetailsResult.asOperation()
                }
            }

        override suspend fun getPastLaunches(query: String): ResponseOperation<List<Launch>> =
            getLaunchesByGroup(
                group = "Past",
                query = query,
            )

        override suspend fun getUpcomingLaunches(query: String): ResponseOperation<List<Launch>> =
            getLaunchesByGroup(
                group = "Upcoming",
                query = query,
            )

        private suspend fun getLaunchesByGroup(
            group: String,
            query: String
        ): ResponseOperation<List<Launch>> =
            withContext(dispatcherProvider.io) {
                launchApi
                    .getLaunchesByGroup(group)
                    .map { output ->
                        val filtered =
                            if (query.isNotBlank()) {
                                output.filter { it.name.contains(query, ignoreCase = true) }
                            } else {
                                output
                            }
                        filtered.map { it.toDomain() }
                    }.asOperation()
            }
    }
