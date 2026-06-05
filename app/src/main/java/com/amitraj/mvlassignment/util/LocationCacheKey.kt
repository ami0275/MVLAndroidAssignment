package com.amitraj.mvlassignment.util

import kotlin.math.round

object LocationCacheKey {
    fun generate(latitude: Double, longitude: Double): String {
        val roundedLat = round(latitude * 1000) / 1000
        val roundedLng = round(longitude * 1000) / 1000
        return "$roundedLat,$roundedLng"
    }
}
