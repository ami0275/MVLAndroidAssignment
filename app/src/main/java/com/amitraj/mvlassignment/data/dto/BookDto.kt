package com.amitraj.mvlassignment.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("name")
    val name: String,
    @SerialName("aqi")
    val aqi: Int? = null
)

@Serializable
data class BookRequestDto(
    @SerialName("a")
    val a: LocationDto,
    @SerialName("b")
    val b: LocationDto
)

@Serializable
data class BookResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("a")
    val a: LocationDto,
    @SerialName("b")
    val b: LocationDto,
    @SerialName("price")
    val price: Int
)
