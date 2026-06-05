package com.amitraj.mvlassignment.di

import android.content.Context
import com.amitraj.mvlassignment.data.api.AqiApiService
import com.amitraj.mvlassignment.data.api.MockAqiApiService
import com.amitraj.mvlassignment.data.api.MockReverseGeocodeApiService
import com.amitraj.mvlassignment.data.api.MockBookingApiService
import com.amitraj.mvlassignment.data.api.ReverseGeocodeApiService
import com.amitraj.mvlassignment.data.api.BookingApiService
import com.amitraj.mvlassignment.data.db.AppDatabase
import com.amitraj.mvlassignment.data.db.dao.LocationCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.waqi.info/")
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAqiApiService(): AqiApiService = MockAqiApiService()

    @Provides
    @Singleton
    fun provideReverseGeocodeApiService(): ReverseGeocodeApiService = MockReverseGeocodeApiService()

    @Provides
    @Singleton
    fun provideBookingApiService(): BookingApiService = MockBookingApiService()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideLocationCacheDao(database: AppDatabase): LocationCacheDao {
        return database.locationCacheDao()
    }
}
