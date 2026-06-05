package com.amitraj.mvlassignment.domain.repository

import com.amitraj.mvlassignment.domain.model.Book

interface BookingRepository {
    suspend fun createBook(
        locationALat: Double,
        locationALng: Double,
        locationAAddress: String,
        locationAAqi: Int?,
        locationBLat: Double,
        locationBLng: Double,
        locationBAddress: String,
        locationBAqi: Int?
    ): Result<Book>
    suspend fun getBooksByMonthYear(year: Int, month: Int): Result<List<Book>>
}

