package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.BookRequestDto
import com.amitraj.mvlassignment.data.dto.BookResponseDto
import com.amitraj.mvlassignment.data.dto.LocationDto
import java.util.UUID

class MockBookingApiService : BookingApiService {
    private val bookingsList = mutableListOf<BookResponseDto>().apply {
        // Initialize with some sample bookings for testing
        for (i in 1..3) {
            add(
                BookResponseDto(
                    id = "MOCK_BOOK_00$i",
                    a = LocationDto(
                        latitude = 37.4979 + (i * 0.01),
                        longitude = 127.0276 + (i * 0.01),
                        name = "Sample Location A - $i",
                        aqi = 35 + i * 15
                    ),
                    b = LocationDto(
                        latitude = 37.5123 + (i * 0.01),
                        longitude = 127.1234 + (i * 0.01),
                        name = "Sample Location B - $i",
                        aqi = 50 + i * 20
                    ),
                    price = 12000 + i * 1500
                )
            )
        }
    }

    override suspend fun createBook(request: BookRequestDto): BookResponseDto {
        val price = 10000 + ((request.a.latitude * 100).toInt() + (request.b.latitude * 100).toInt()) % 5000
        val response = BookResponseDto(
            id = UUID.randomUUID().toString(),
            a = request.a,
            b = request.b,
            price = price
        )
        bookingsList.add(response)
        return response
    }

    override suspend fun getBooks(year: Int, month: Int): List<BookResponseDto> {
        return bookingsList
    }
}

