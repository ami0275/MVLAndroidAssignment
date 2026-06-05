package com.amitraj.mvlassignment.data.repository

import com.amitraj.mvlassignment.domain.model.Book
import com.amitraj.mvlassignment.domain.model.Location
import com.amitraj.mvlassignment.domain.repository.BookingRepository
import com.amitraj.mvlassignment.data.api.BookingApiService
import com.amitraj.mvlassignment.data.dto.LocationDto
import com.amitraj.mvlassignment.data.dto.BookRequestDto
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingApiService: BookingApiService
) : BookingRepository {

    override suspend fun createBook(
        locationALat: Double,
        locationALng: Double,
        locationAAddress: String,
        locationAAqi: Int?,
        locationBLat: Double,
        locationBLng: Double,
        locationBAddress: String,
        locationBAqi: Int?
    ): Result<Book> {
        return try {
            // Note: In a real scenario, we would get proper addresses from somewhere
            // For now, we'll use the coordinates as address
            val request = BookRequestDto(
                a = LocationDto(
                    latitude = locationALat,
                    longitude = locationALng,
                    name = locationAAddress,
                    aqi = locationAAqi
                ),
                b = LocationDto(
                    latitude = locationBLat,
                    longitude = locationBLng,
                    name = locationBAddress,
                    aqi = locationBAqi
                )
            )
            
            val response = bookingApiService.createBook(request)
            
            val book = Book(
                id = response.id,
                locationA = Location(
                    latitude = response.a.latitude,
                    longitude = response.a.longitude,
                    address = response.a.name,
                    aqi = response.a.aqi
                ),
                locationB = Location(
                    latitude = response.b.latitude,
                    longitude = response.b.longitude,
                    address = response.b.name,
                    aqi = response.b.aqi
                ),
                price = response.price
            )
            
            Result.success(book)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBooksByMonthYear(year: Int, month: Int): Result<List<Book>> {
        return try {
            val responses = bookingApiService.getBooks(year, month)
            
            val books = responses.map { response ->
                Book(
                    id = response.id,
                    locationA = Location(
                        latitude = response.a.latitude,
                        longitude = response.a.longitude,
                        address = response.a.name,
                        aqi = response.a.aqi
                    ),
                    locationB = Location(
                        latitude = response.b.latitude,
                        longitude = response.b.longitude,
                        address = response.b.name,
                        aqi = response.b.aqi
                    ),
                    price = response.price
                )
            }
            
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
