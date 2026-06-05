package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.ReverseGeocodeResponseDto
import com.amitraj.mvlassignment.data.dto.LocalityInfoDto
import com.amitraj.mvlassignment.data.dto.AdministrativeDto
import kotlin.random.Random

class MockReverseGeocodeApiService : ReverseGeocodeApiService {
    private val cities = listOf(
        listOf("Seocho District", "Yangjae 2(i)-dong"),
        listOf("Gangnam District", "Apgujeong-dong"),
        listOf("Songpa District", "Sinchon-dong"),
        listOf("Mapo District", "Hongdae-gu"),
        listOf("Jung District", "Myeongdong"),
        listOf("Jongno District", "Gwanghwamun"),
        listOf("Dongdaemun District", "Dongdaemun-gu"),
        listOf("Seongbuk District", "Suyu-dong")
    )

    override suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double,
        language: String
    ): ReverseGeocodeResponseDto {
        val randomCities = cities[Random.nextInt(cities.size)]
        
        val administrative = randomCities.mapIndexed { index, name ->
            AdministrativeDto(
                name = name,
                order = randomCities.size - index // Descending order
            )
        }
        
        return ReverseGeocodeResponseDto(
            localityInfo = LocalityInfoDto(
                administrative = administrative
            )
        )
    }
}
