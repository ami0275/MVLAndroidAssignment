package com.amitraj.mvlassignment.data.model

import kotlinx.serialization.Serializable

/**
 * Location data model representing a geographical position with location info and air quality
 */
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val aqi: Int,
    val name: String
)

/**
 * Booking information with location A and B details
 */
@Serializable
data class BookingInfo(
    val a: Location,
    val b: Location,
    val price: Double? = null,
    val id: String? = null
)

/**
 * Location with nickname support
 */
data class LocationWithNickname(
    val location: Location,
    val nickname: String? = null
) {
    fun getDisplayName(): String = nickname ?: location.name
}

/**
 * UI state for booking
 */
data class BookingUIState(
    val locationA: Location? = null,
    val locationB: Location? = null,
    val nicknameA: String? = null,
    val nicknameB: String? = null,
    val bookingResult: BookingInfo? = null,
    val bookingHistory: List<BookingInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun getDisplayNameA(): String = nicknameA ?: locationA?.name ?: "Set A"
    fun getDisplayNameB(): String = nicknameB ?: locationB?.name ?: "Set B"
}
