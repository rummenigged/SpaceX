package com.octopus.edu.kotlin.core.data.launches

interface LaunchesApi {
    @retrofit2.http.GET("launches")
    suspend fun getLaunches(): com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse<List<LaunchDTO>>

    @retrofit2.http.GET("launches/{flight_number}")
    suspend fun getLaunchDetails(
        @retrofit2.http.Path("flight_number") flightNumber: Int,
    ): com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse<LaunchDTO>
}
