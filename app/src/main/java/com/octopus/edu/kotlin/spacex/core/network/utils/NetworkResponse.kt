package com.octopus.edu.kotlin.spacex.core.network.utils

import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation.Error
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation.Success
import com.octopus.edu.kotlin.spacex.core.network.utils.NetworkResponse.ApiError
import okhttp3.ResponseBody
import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    data class Success<out T : Any>(
        val data: T,
    ) : NetworkResponse<T>()

    data class NetworkError(
        val error: IOException,
    ) : NetworkResponse<Nothing>()

    data class ApiError(
        val body: ResponseBody?,
        val code: Int? = null,
        val throwable: Throwable? = null,
    ) : NetworkResponse<Nothing>()

    fun <R : Any> map(mapper: (T) -> R) =
        when (this) {
            is Success -> Success(mapper(data))
            is NetworkError -> NetworkError(error)
            is ApiError -> ApiError(body, code, throwable)
        }
}

fun <T : Any> NetworkResponse<T>.asOperation(): ResponseOperation<T> =
    when (this) {
        is NetworkResponse.Success -> Success(data)
        is NetworkResponse.NetworkError -> Error(message = error.message ?: "", isNetworkError = true)
        is ApiError -> Error(message = body?.string() ?: "Unknown Error", code = code ?: 0)
    }
