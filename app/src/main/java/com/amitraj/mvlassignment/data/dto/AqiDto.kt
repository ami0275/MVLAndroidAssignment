package com.amitraj.mvlassignment.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AqiResponseDto(
    @SerialName("data")
    val data: AqiDataDto?
)

@Serializable
data class AqiDataDto(
    @SerialName("aqi")
    val aqi: Int,
    @SerialName("status")
    val status: String? = null
)
