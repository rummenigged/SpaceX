package com.octopus.edu.kotlin.spacex.utils

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

fun NavController.safelyNavigate(direction: NavDirections) =
    try {
        navigate(direction)
    } catch (e: Exception) {
        Log.e(e::class.toString(), e.message.orEmpty())
    }

fun Fragment.safelyNavigate(
    direction: NavDirections,
    @IdRes host: Int? = null,
) {
    val navController =
        if (host == null) {
            findNavController()
        } else {
            Navigation.findNavController(requireActivity(), host)
        }

    navController.safelyNavigate(direction)
}
