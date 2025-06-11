package com.octopus.edu.kotlin.core.data.launches.dto

import com.octopus.edu.kotlin.core.data.launches.utils.DateTimeUtils
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchDTO(
    @Json(name = "flight_number")
    val flightNumber: Int,
    @Json(name = "mission_name")
    val name: String,
    @Json(name = "launch_date_local")
    val date: String,
    @Json(name = "rocket")
    val rocket: RocketDTO,
    @Json(name = "launch_success")
    val isLaunchSuccess: Boolean? = false,
    @Json(name = "launch_failure_det    ails")
    val launchFailureDetails: LaunchFailureDetails?,
    @Json(name = "launch_site")
    val site: Site?,
    val links: Links,
) {
    @JsonClass(generateAdapter = true)
    data class RocketDTO(
        @Json(name = "rocket_id")
        val id: String?,
        @Json(name = "rocket_name")
        val name: String?,
    )

    @JsonClass(generateAdapter = true)
    data class LaunchFailureDetails(
        val reason: String?,
    )

    @JsonClass(generateAdapter = true)
    data class Links(
        @Json(name = "mission_patch_small")
        val patch: String?,
    )

    @JsonClass(generateAdapter = true)
    data class Site(
        @Json(name = "site_id")
        val id: String,
        @Json(name = "site_name")
        val name: String,
        @Json(name = "site_name_long")
        val longName: String,
    )
}

fun LaunchDTO.toDomain(): Launch =
    Launch(
        missionName = name,
        flightNumber = flightNumber,
        date =
            DateTimeUtils
                .convertDate(
                    date,
                    finalFormat = DateTimeUtils.DateFormat.LONG_DATE_AND_TIME_US,
                ).orEmpty(),
        rocketName = this.rocket.name.orEmpty(),
        launchStatus =
            if (isLaunchSuccess == true) {
                LaunchStatus.Success
            } else {
                LaunchStatus
                    .Failure(launchFailureDetails?.reason.orEmpty())
            },
        patch = links.patch,
    )

fun LaunchDTO.toDomain(rocket: RocketDetailsDTO): LaunchDetails =
    LaunchDetails(
        missionName = name,
        flightNumber = flightNumber,
        date = date,
        siteName = site?.longName.orEmpty(),
        rocket = rocket.toDomain(),
        if (isLaunchSuccess == true) {
            LaunchStatus.Success
        } else {
            LaunchStatus.Failure(
                launchFailureDetails?.reason.orEmpty(),
            )
        },
        patch = links.patch,
    )
