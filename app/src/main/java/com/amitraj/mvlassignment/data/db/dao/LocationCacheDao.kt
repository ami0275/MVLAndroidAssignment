package com.amitraj.mvlassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amitraj.mvlassignment.data.db.entity.LocationCacheEntity

@Dao
interface LocationCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationCacheEntity)
    
    @Query("SELECT * FROM location_cache WHERE location_key = :key")
    suspend fun getLocation(key: String): LocationCacheEntity?
    
    @Query("DELETE FROM location_cache")
    suspend fun clearCache()
}
