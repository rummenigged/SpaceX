package com.octopus.edu.kotlin.core.ui.common

interface SpaceXNavigation {
    fun navigate(destination: SpaceXDestination)
}

sealed interface SpaceXDestination {
    data class LaunchDetails(
        val flightNumber: Int,
    ) : SpaceXDestination
}
