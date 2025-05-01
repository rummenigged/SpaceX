package com.octopus.edu.kotlin.core.data.launches.api

import com.octopus.edu.kotlin.core.data.launches.dto.LaunchDTO
import com.octopus.edu.kotlin.core.network.utils.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LaunchesApi {
    @GET("launches")
    suspend fun getLaunches(): NetworkResponse<List<LaunchDTO>>

    @GET("launches/{launch_group}")
    suspend fun getLaunchesByGroup(
        @Path("launch_group") group: String
    ): NetworkResponse<List<LaunchDTO>>

    @GET("launches/{flight_number}")
    suspend fun getLaunchDetails(
        @Path("flight_number") flightNumber: Int
    ): NetworkResponse<LaunchDTO>
}
