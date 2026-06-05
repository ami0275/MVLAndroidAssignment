package com.amitraj.mvlassignment.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amitraj.mvlassignment.data.db.dao.LocationCacheDao
import com.amitraj.mvlassignment.data.db.entity.LocationCacheEntity

@Database(
    entities = [LocationCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationCacheDao(): LocationCacheDao

    companion object {
        private const val DATABASE_NAME = "mvl_assignment_db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}
