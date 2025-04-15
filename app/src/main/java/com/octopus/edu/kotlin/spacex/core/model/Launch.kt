package com.octopus.edu.kotlin.spacex.core.model

sealed class LaunchStatus(){
    data object Success: LaunchStatus()
    data class Failure(val reason: String): LaunchStatus()
}
data class Launch(
    val missionName: String,
    val flightNumber: Int,
    val date: String,
    val rocketName: String,
    val launchStatus: LaunchStatus,
    val patch: String?
) {
    companion object
}

fun Launch.Companion.mock(): Launch = Launch(
    missionName = "Mock Name",
    flightNumber = 0,
    date = "Mock Date",
    rocketName = "Mock Rocket Name",
    launchStatus = LaunchStatus.Success,
    patch = null
)