package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.ReverseGeocodeResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeocodeApiService {
    @GET("reverse-geocode-to-city")
    suspend fun reverseGeocode(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") language: String = "en"
    ): ReverseGeocodeResponseDto
}
