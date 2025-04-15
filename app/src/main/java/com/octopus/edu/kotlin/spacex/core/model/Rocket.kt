package com.octopus.edu.kotlin.spacex.core.model

data class Rocket(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val images: List<String>,
){
    companion object
}

fun Rocket.Companion.mock(): Rocket = Rocket(
    id = "rocket1",
    name = "Rocket 1",
    isActive = true,
    images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
)