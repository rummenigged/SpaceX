package com.octopus.edu.kotlin.spacex.core.ui

interface SpaceXNavigation {
    fun navigate(destination: SpaceXDestination)
}

sealed interface SpaceXDestination {
    data class LaunchDetails(val flightNumber: Int) : SpaceXDestination
}
