package com.octopus.edu.kotlin.core.data.launches.api

import com.octopus.edu.kotlin.core.data.launches.dto.RocketDetailsDTO
import com.octopus.edu.kotlin.core.network.utils.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RocketApi {
    @GET("rockets/{rocket_id}")
    suspend fun getRocketDetails(
        @Path("rocket_id") rocketId: String,
    ): NetworkResponse<RocketDetailsDTO>
}
