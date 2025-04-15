package com.octopus.edu.kotlin.spacex.core.model

sealed class ResponseOperation<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResponseOperation<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val isNetworkError: Boolean = false) : ResponseOperation<Nothing>()
}