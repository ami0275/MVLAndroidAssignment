package com.amitraj.mvlassignment.presentation.navigation

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Nickname : Screen("nickname/{latitude}/{longitude}/{address}") {
        fun createRoute(latitude: Double, longitude: Double, address: String): String {
            return "nickname/$latitude/$longitude/$address"
        }
    }
    object BookingResult : Screen("booking_result")
    object History : Screen("history")
}
