package com.octopus.edu.kotlin.core.network.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class NetworkResultCall<T : Any>(
    private val call: Call<T>,
) : Call<NetworkResponse<T>> {
    override fun enqueue(callback: Callback<NetworkResponse<T>>) =
        call.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    val code = response.code()

                    with(this@NetworkResultCall) {
                        when {
                            response.isSuccessful -> {
                                val body = response.body()

                                if (body == null) {
                                    callback.onResponse(
                                        this,
                                        Response.success(
                                            NetworkResponse.ApiError(
                                                body,
                                                code,
                                            ),
                                        ),
                                    )

                                    return@with
                                }

                                callback.onResponse(
                                    this,
                                    Response.success(NetworkResponse.Success(body)),
                                )
                            }

                            else -> {
                                callback.onResponse(
                                    this,
                                    Response.success(
                                        NetworkResponse.ApiError(
                                            response.errorBody(),
                                            code,
                                        ),
                                    ),
                                )
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<T?>,
                    throwable: Throwable,
                ) {
                    val error =
                        when (throwable) {
                            is UnknownHostException -> NetworkResponse.NetworkError(throwable)
                            else -> NetworkResponse.ApiError(body = null, throwable = throwable)
                        }

                    callback.onFailure(this@NetworkResultCall, throwable)
                }
            },
        )

    override fun execute(): Response<NetworkResponse<T>?> =
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

    override fun isExecuted() = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled() = call.isCanceled

    override fun clone() = NetworkResultCall(call.clone())

    override fun request() = call.request()

    override fun timeout() = call.timeout()
}
