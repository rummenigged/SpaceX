package com.octopus.edu.kotlin.core.data.launches

interface RocketApi {
    @retrofit2.http.GET("rockets/{rocket_id}")
    suspend fun getRocketDetails(
        @retrofit2.http.Path("rocket_id") rocketId: String,
    ): com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse<RocketDetailsDTO>
}
