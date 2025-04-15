package com.octopus.edu.kotlin.spacex.core.network.utils

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResultCallAdapter<T: Any>(
    private val successType: Type
) : CallAdapter<T, Call<NetworkResponse<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<NetworkResponse<T>> = NetworkResultCall(call)
}