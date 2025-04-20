package com.octopus.edu.kotlin.spacex.core.data

import com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LaunchesApi {
    @GET("launches")
    suspend fun getLaunches(): NetworkResponse<List<LaunchDTO>>

    @GET("launches/{flight_number}")
    suspend fun getLaunchDetails(
        @Path("flight_number") flightNumber: Int,
    ): NetworkResponse<LaunchDTO>
}
