package com.amitraj.mvlassignment.util

import com.amitraj.mvlassignment.data.dto.ReverseGeocodeResponseDto

object AddressFormatter {
    fun format(response: ReverseGeocodeResponseDto): String {
        val administrative = response.localityInfo?.administrative
            ?.sortedByDescending { it.order ?: 0 }
            ?.take(2)
            ?.sortedBy { it.order ?: 0 }
            ?.mapNotNull { it.name }
            ?: emptyList()

        return administrative.joinToString(", ")
    }
}
