package com.amitraj.mvlassignment.di

import com.amitraj.mvlassignment.data.repository.LocationRepositoryImpl
import com.amitraj.mvlassignment.data.repository.BookingRepositoryImpl
import com.amitraj.mvlassignment.domain.repository.LocationRepository
import com.amitraj.mvlassignment.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(impl: BookingRepositoryImpl): BookingRepository
}
