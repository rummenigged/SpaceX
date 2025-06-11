package com.octopus.edu.kotlin.core.domain.models.launch

import com.octopus.edu.kotlin.core.domain.models.rocket.Rocket
import com.octopus.edu.kotlin.core.domain.models.rocket.mock

data class LaunchDetails(
    val missionName: String,
    val flightNumber: Int,
    val date: String,
    val siteName: String,
    val rocket: Rocket,
    val launchStatus: LaunchStatus,
    val patch: String?,
) {
    companion object
}

fun LaunchDetails.Companion.mock(id: Int = 0): LaunchDetails =
    LaunchDetails(
        missionName = "Mock Name $id",
        flightNumber = id,
        date = "$id/$id/20$id",
        siteName = "Site Name $id",
        rocket = Rocket.Companion.mock(),
        launchStatus = LaunchStatus.Success,
        patch = null,
    )
