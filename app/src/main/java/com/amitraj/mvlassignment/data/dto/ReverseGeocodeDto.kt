package com.amitraj.mvlassignment.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodeResponseDto(
    @SerialName("localityInfo")
    val localityInfo: LocalityInfoDto?
)

@Serializable
data class LocalityInfoDto(
    @SerialName("administrative")
    val administrative: List<AdministrativeDto>? = null
)

@Serializable
data class AdministrativeDto(
    @SerialName("name")
    val name: String?,
    @SerialName("order")
    val order: Int?
)
