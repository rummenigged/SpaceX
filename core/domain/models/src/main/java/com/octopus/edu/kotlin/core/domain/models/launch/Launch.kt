package com.octopus.edu.kotlin.core.domain.models.launch

sealed class LaunchStatus {
    data object Success : LaunchStatus()

    data class Failure(val reason: String) : LaunchStatus()
}

data class Launch(
    val missionName: String,
    val flightNumber: Int,
    val date: String,
    val rocketName: String,
    val launchStatus: LaunchStatus,
    val patch: String?,
) {
    companion object
}

fun Launch.Companion.mock(id: Int = 0): Launch =
    Launch(
        missionName = "Mock Name $id",
        flightNumber = id,
        date = "$id/$id/$id",
        rocketName = "Mock Rocket Name $id",
        launchStatus = LaunchStatus.Success,
        patch = null,
    )

fun Launch.Companion.mockList(amount: Int): List<Launch> = (0 until amount).map { mock(it) }
