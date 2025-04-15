package com.octopus.edu.kotlin.spacex.core.data

import com.octopus.edu.kotlin.spacex.core.model.Rocket
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RocketDetailsDTO(
    @Json(name = "rocket_id")
    val id: String,
    @Json(name = "rocket_name")
    val name: String,
    @Json(name = "active")
    val isActive: Boolean,
    @Json(name = "flickr_images")
    val images: List<String>
)

fun RocketDetailsDTO.toDomain(): Rocket = Rocket(
    id = id,
    name = name,
    isActive = isActive,
    images = images
)
