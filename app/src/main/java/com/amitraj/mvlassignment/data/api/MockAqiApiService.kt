package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.AqiResponseDto
import com.amitraj.mvlassignment.data.dto.AqiDataDto
import kotlin.random.Random

class MockAqiApiService : AqiApiService {
    override suspend fun getAqi(
        latitude: Double,
        longitude: Double,
        token: String
    ): AqiResponseDto {
        // Mock AQI data between 0-500
        val mockAqi = Random.nextInt(0, 501)
        return AqiResponseDto(
            data = AqiDataDto(
                aqi = mockAqi,
                status = "ok"
            )
        )
    }
}
