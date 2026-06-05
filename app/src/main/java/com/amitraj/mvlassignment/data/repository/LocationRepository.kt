package com.amitraj.mvlassignment.data.repository

import com.amitraj.mvlassignment.data.model.BookingInfo
import com.amitraj.mvlassignment.data.model.Location
import kotlin.math.round
import java.time.LocalDate

/**
 * Repository for managing location, air quality, and booking data
 * Mocked API responses - replace with real API calls in production
 */
class LocationRepository {

    /**
     * Extract address name from latitude and longitude
     * Simulates reverse geocoding from BigDataCloud API
     */
    suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double): String {
        // Mock data based on coordinate position
        return when {
            latitude in 37.4..37.6 && longitude in 126.9..127.1 -> {
                val districts = listOf(
                    "Seocho District, Yangjae 2(i)-dong",
                    "Songpa District, Samseong 1(il)-dong",
                    "Gangnam District, Cheongdam-dong",
                    "Guro District, Guro 3(sam)-dong"
                )
                val idx = ((latitude * 100).toInt() + (longitude * 100).toInt()) % districts.size
                districts[idx]
            }
            else -> "Seoul Location (${roundToThreeDecimals(latitude)}, ${roundToThreeDecimals(longitude)})"
        }
    }

    /**
     * Fetch air quality information from coordinates
     * Simulates AQICN API response
     */
    suspend fun getAirQualityFromCoordinates(latitude: Double, longitude: Double): Int {
        // Mock AQI based on coordinates
        val baseAqi = 25 + ((latitude * 10).toInt() + (longitude * 10).toInt()) % 50
        return baseAqi.coerceIn(0, 500)
    }

    /**
     * Create Location object from coordinates and fetched data
     */
    suspend fun createLocation(latitude: Double, longitude: Double): Location {
        val addressName = getAddressFromCoordinates(latitude, longitude)
        val aqi = getAirQualityFromCoordinates(latitude, longitude)

        return Location(
            latitude = roundToThreeDecimals(latitude),
            longitude = roundToThreeDecimals(longitude),
            aqi = aqi,
            name = addressName
        )
    }

    /**
     * Post booking request and get mocked response with price
     */
    suspend fun postBooking(bookingInfo: BookingInfo): BookingInfo {
        // Mock price calculation
        val mockPrice = 10000.0 + ((bookingInfo.a.latitude * 100).toInt() + (bookingInfo.b.latitude * 100).toInt()) % 5000
        val mockId = "BOOK_${System.currentTimeMillis()}"
        
        return bookingInfo.copy(price = mockPrice, id = mockId)
    }

    /**
     * Get booking history for a specific month
     * Mocks GET /books?year=$year&month=$month endpoint
     */
    suspend fun getBookingHistory(year: Int, month: Int): List<BookingInfo> {
        // Mock response with 2 sample bookings
        return listOf(
            BookingInfo(
                a = Location(36.564, 127.001, 30, "Seoul A Location"),
                b = Location(36.567, 127.0, 40, "Seoul B Location"),
                price = 10000.0,
                id = "BOOK_001"
            ),
            BookingInfo(
                a = Location(36.577, 127.033, 50, "Seoul C Location"),
                b = Location(36.567, 127.0, 60, "Seoul D Location"),
                price = 20000.0,
                id = "BOOK_002"
            ),
        )
    }

    /**
     * Round latitude/longitude to 3 decimal places for caching
     */
    fun roundToThreeDecimals(value: Double): Double {
        return round(value * 1000) / 1000
    }

    /**
     * Check if two coordinates are the same (up to 3 decimal places)
     */
    fun isSameLocation(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        return roundToThreeDecimals(lat1) == roundToThreeDecimals(lat2) &&
                roundToThreeDecimals(lon1) == roundToThreeDecimals(lon2)
    }

    /**
     * Simulate location cache (optional enhancement)
     */
    private val locationCache = mutableMapOf<String, Location>()

    fun getCachedLocation(latitude: Double, longitude: Double): Location? {
        val key = "${roundToThreeDecimals(latitude)}_${roundToThreeDecimals(longitude)}"
        return locationCache[key]
    }

    fun cacheLocation(location: Location) {
        val key = "${location.latitude}_${location.longitude}"
        locationCache[key] = location
    }
}
