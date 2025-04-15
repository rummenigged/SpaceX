package com.octopus.edu.kotlin.spacex.core.data

import com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RocketApi {

    @GET("rockets/{rocket_id}")
    suspend fun getRocketDetails(@Path("rocket_id") rocketId: String): NetworkResponse<RocketDetailsDTO>
}