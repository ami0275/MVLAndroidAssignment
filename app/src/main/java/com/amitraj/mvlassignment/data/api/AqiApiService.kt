package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.AqiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AqiApiService {
    @GET("geolocalized/feed")
    suspend fun getAqi(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("token") token: String = "demo"
    ): AqiResponseDto
}
