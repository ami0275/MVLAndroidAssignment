package com.amitraj.mvlassignment.data.api

import com.amitraj.mvlassignment.data.dto.BookRequestDto
import com.amitraj.mvlassignment.data.dto.BookResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookingApiService {
    @POST("books")
    suspend fun createBook(@Body request: BookRequestDto): BookResponseDto

    @GET("books")
    suspend fun getBooks(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<BookResponseDto>
}
