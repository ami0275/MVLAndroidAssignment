package com.amitraj.mvlassignment.domain.repository

import com.amitraj.mvlassignment.domain.model.AqiData
import com.amitraj.mvlassignment.domain.model.ReverseGeocodeResult

interface LocationRepository {
    suspend fun fetchAqi(latitude: Double, longitude: Double): Result<AqiData>
    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<ReverseGeocodeResult>
    suspend fun saveLocationNickname(latitude: Double, longitude: Double, nickname: String)
    suspend fun getLocationNickname(latitude: Double, longitude: Double): String?
}
