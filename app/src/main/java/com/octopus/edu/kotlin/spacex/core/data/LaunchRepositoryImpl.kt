package com.octopus.edu.kotlin.spacex.core.data

import com.octopus.edu.kotlin.spacex.core.model.Launch
import com.octopus.edu.kotlin.spacex.core.model.LaunchDetails
import com.octopus.edu.kotlin.spacex.core.model.LaunchStatus
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation
import com.octopus.edu.kotlin.spacex.core.model.Rocket
import com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse
import com.octopus.edu.kotlin.spacex.core.network.utils.asOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LaunchRepositoryImpl @Inject constructor(
    private val launchApi: LaunchesApi,
    private val rocketApi: RocketApi): LaunchRepository {
    override suspend  fun getAllLaunches(): ResponseOperation<List<Launch>> = withContext(
        Dispatchers.IO
    ){
        launchApi.getLaunches()
        .map { output -> output.map { launch -> launch.toDomain() } }
        .asOperation()
    }

    override suspend  fun getLaunchDetails(flightNumber: Int): ResponseOperation<LaunchDetails> =
        withContext(Dispatchers.IO){
            when(val launchDetailsResult = launchApi.getLaunchDetails(flightNumber)){
                is NetworkResponse.Success -> {
                    val launch = launchDetailsResult.data
                    when (val rocketDetailsResult = rocketApi.getRocketDetails(launch.rocket.id ?: "")) {
                        is NetworkResponse.Success -> {
                            val rocket = rocketDetailsResult.data
                            val result = LaunchDetails(
                                missionName = launch.name,
                                flightNumber = launch.flightNumber,
                                date = launch.date,
                                siteName = launch.site?.longName.orEmpty(),
                                rocket = rocket.toDomain(),
                                if (launch.isLaunchSuccess == true) LaunchStatus.Success
                                else LaunchStatus.Failure(launch.launchFailureDetails?.reason.orEmpty()),
                                patch = launch.links.patch
                            )
                            NetworkResponse<LaunchDetails>.Success(result).asOperation()
                        }
                        is NetworkResponse.ApiError,
                        is NetworkResponse.NetworkError -> rocketDetailsResult.asOperation()
                    }
                }
                is NetworkResponse.ApiError,
                is NetworkResponse.NetworkError -> launchDetailsResult.asOperation()
            }
        }

}