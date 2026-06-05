package com.amitraj.mvlassignment.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_cache")
data class LocationCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "location_key")
    val locationKey: String, // Composite key: "${lat_rounded},${lng_rounded}"
    
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    
    @ColumnInfo(name = "address")
    val address: String,
    
    @ColumnInfo(name = "aqi")
    val aqi: Int?,
    
    @ColumnInfo(name = "nickname")
    val nickname: String?,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
