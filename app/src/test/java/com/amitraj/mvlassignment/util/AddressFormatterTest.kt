package com.amitraj.mvlassignment.util

import com.amitraj.mvlassignment.data.dto.AdministrativeDto
import com.amitraj.mvlassignment.data.dto.LocalityInfoDto
import com.amitraj.mvlassignment.data.dto.ReverseGeocodeResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressFormatterTest {

    @Test
    fun testAddressFormatting_withSampleCoordinatesData() {
        val mockResponse = ReverseGeocodeResponseDto(
            localityInfo = LocalityInfoDto(
                administrative = listOf(
                    AdministrativeDto(name = "South Korea", order = 2),
                    AdministrativeDto(name = "Seoul", order = 3),
                    AdministrativeDto(name = "Seocho District", order = 4),
                    AdministrativeDto(name = "Yangjae 2(i)-dong", order = 5)
                )
            )
        )

        val formattedAddress = AddressFormatter.format(mockResponse)
        assertEquals("Seocho District, Yangjae 2(i)-dong", formattedAddress)
    }

    @Test
    fun testAddressFormatting_withFewerThanTwoEntries() {
        val mockResponse = ReverseGeocodeResponseDto(
            localityInfo = LocalityInfoDto(
                administrative = listOf(
                    AdministrativeDto(name = "South Korea", order = 2)
                )
            )
        )

        val formattedAddress = AddressFormatter.format(mockResponse)
        assertEquals("South Korea", formattedAddress)
    }

    @Test
    fun testAddressFormatting_withNullLocalityInfo() {
        val mockResponse = ReverseGeocodeResponseDto(localityInfo = null)
        val formattedAddress = AddressFormatter.format(mockResponse)
        assertEquals("", formattedAddress)
    }
}
