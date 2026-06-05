package com.amitraj.mvlassignment.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val aqi: Int? = null,
    val nickname: String? = null
)
