package com.octopus.edu.kotlin.core.data.launches

import com.octopus.edu.kotlin.spacex.core.model.Rocket

@com.squareup.moshi.JsonClass(generateAdapter = true)
data class RocketDetailsDTO(
    @com.squareup.moshi.Json(name = "rocket_id")
    val id: String,
    @com.squareup.moshi.Json(name = "rocket_name")
    val name: String,
    @com.squareup.moshi.Json(name = "active")
    val isActive: Boolean,
    @com.squareup.moshi.Json(name = "flickr_images")
    val images: List<String>,
)

fun RocketDetailsDTO.toDomain(): com.octopus.edu.kotlin.spacex.core.model.Rocket =
    com.octopus.edu.kotlin.spacex.core.model.Rocket(
        id = id,
        name = name,
        isActive = isActive,
        images = images,
    )
