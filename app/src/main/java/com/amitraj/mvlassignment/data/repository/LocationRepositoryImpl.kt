package com.amitraj.mvlassignment.data.repository

import com.amitraj.mvlassignment.domain.model.AqiData
import com.amitraj.mvlassignment.domain.model.ReverseGeocodeResult
import com.amitraj.mvlassignment.domain.repository.LocationRepository
import com.amitraj.mvlassignment.data.api.AqiApiService
import com.amitraj.mvlassignment.data.api.ReverseGeocodeApiService
import com.amitraj.mvlassignment.data.db.dao.LocationCacheDao
import com.amitraj.mvlassignment.data.db.entity.LocationCacheEntity
import com.amitraj.mvlassignment.util.AddressFormatter
import com.amitraj.mvlassignment.util.LocationCacheKey
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val aqiApiService: AqiApiService,
    private val reverseGeocodeApiService: ReverseGeocodeApiService,
    private val locationCacheDao: LocationCacheDao
) : LocationRepository {

    override suspend fun fetchAqi(latitude: Double, longitude: Double): Result<AqiData> {
        return try {
            val cacheKey = LocationCacheKey.generate(latitude, longitude)
            val cached = locationCacheDao.getLocation(cacheKey)
            
            if (cached != null && cached.aqi != null) {
                return Result.success(AqiData(aqi = cached.aqi, status = "cached"))
            }

            val response = aqiApiService.getAqi(latitude, longitude)
            val aqi = response.data?.aqi ?: 0
            
            if (cached != null) {
                locationCacheDao.insertLocation(cached.copy(aqi = aqi))
            } else {
                locationCacheDao.insertLocation(
                    LocationCacheEntity(
                        locationKey = cacheKey,
                        latitude = latitude,
                        longitude = longitude,
                        address = "",
                        aqi = aqi,
                        nickname = null
                    )
                )
            }
            
            Result.success(AqiData(aqi = aqi, status = "ok"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<ReverseGeocodeResult> {
        return try {
            val cacheKey = LocationCacheKey.generate(latitude, longitude)
            val cached = locationCacheDao.getLocation(cacheKey)
            
            if (cached != null && cached.address.isNotEmpty()) {
                return Result.success(ReverseGeocodeResult(address = cached.address))
            }

            val response = reverseGeocodeApiService.reverseGeocode(latitude, longitude)
            val address = AddressFormatter.format(response)
            
            if (cached != null) {
                locationCacheDao.insertLocation(cached.copy(address = address))
            } else {
                locationCacheDao.insertLocation(
                    LocationCacheEntity(
                        locationKey = cacheKey,
                        latitude = latitude,
                        longitude = longitude,
                        address = address,
                        aqi = null,
                        nickname = null
                    )
                )
            }
            
            Result.success(ReverseGeocodeResult(address = address))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveLocationNickname(latitude: Double, longitude: Double, nickname: String) {
        val cacheKey = LocationCacheKey.generate(latitude, longitude)
        val cached = locationCacheDao.getLocation(cacheKey)
        
        if (cached != null) {
            locationCacheDao.insertLocation(
                cached.copy(nickname = nickname)
            )
        } else {
            // If no cached address yet, we'll save nickname for later
            locationCacheDao.insertLocation(
                LocationCacheEntity(
                    locationKey = cacheKey,
                    latitude = latitude,
                    longitude = longitude,
                    address = "",
                    aqi = null,
                    nickname = nickname
                )
            )
        }
    }

    override suspend fun getLocationNickname(latitude: Double, longitude: Double): String? {
        val cacheKey = LocationCacheKey.generate(latitude, longitude)
        return locationCacheDao.getLocation(cacheKey)?.nickname
    }
}
