package com.octopus.edu.kotlin.spacex.core.network.utils

import okhttp3.Request
import okio.Timeout
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

    override fun execute(): Response<NetworkResponse<T>?> {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun clone(): Call<NetworkResponse<T>?> {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }
}
