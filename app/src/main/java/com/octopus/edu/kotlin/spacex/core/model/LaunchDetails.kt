package com.octopus.edu.kotlin.spacex.core.model

data class LaunchDetails(
    val missionName: String,
    val flightNumber: Int,
    val date: String,
    val siteName: String,
    val rocket: Rocket,
    val launchStatus: LaunchStatus,
    val patch: String?
) {
    companion object
}

fun LaunchDetails.Companion.mock(): LaunchDetails = LaunchDetails(
    missionName = "Mock Name",
    flightNumber = 0,
    date = "Mock Date",
    siteName = "Site Name",
    rocket = Rocket.mock(),
    launchStatus = LaunchStatus.Success,
    patch = null
)