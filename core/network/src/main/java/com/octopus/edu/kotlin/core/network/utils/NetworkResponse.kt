package com.octopus.edu.kotlin.core.network.utils

import com.octopus.edu.kotlin.core.domain.common.ResponseOperation
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Error
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Success
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
        is NetworkResponse.NetworkError ->
            Error(
                message = error.message ?: "",
                isNetworkError = true,
            )
        is NetworkResponse.ApiError -> Error(message = ResponseBody.toString(), code = code ?: 0)
    }
