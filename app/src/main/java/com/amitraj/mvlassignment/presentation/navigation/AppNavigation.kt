package com.amitraj.mvlassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.amitraj.mvlassignment.presentation.screen.MapScreen
import com.amitraj.mvlassignment.presentation.screen.NicknameScreen
import com.amitraj.mvlassignment.presentation.screen.BookingResultScreen
import com.amitraj.mvlassignment.presentation.screen.HistoryScreen
import com.amitraj.mvlassignment.presentation.viewmodel.MapViewModel
import com.amitraj.mvlassignment.presentation.viewmodel.NicknameViewModel
import com.amitraj.mvlassignment.presentation.viewmodel.BookingViewModel
import com.amitraj.mvlassignment.presentation.viewmodel.HistoryViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val mapViewModel: MapViewModel = hiltViewModel()
    val bookingViewModel: BookingViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Map.route
    ) {
        composable(Screen.Map.route) {
            MapScreen(
                viewModel = mapViewModel,
                onNavigateToNickname = { lat, lng, address ->
                    navController.navigate(Screen.Nickname.createRoute(lat, lng, address))
                },
                onNavigateToBooking = { locationALat, locationALng, locationAAddr, locationAAqi, locationBLat, locationBLng, locationBAddr, locationBAqi ->
                    bookingViewModel.createBook(
                        locationALat, locationALng, locationAAddr, locationAAqi,
                        locationBLat, locationBLng, locationBAddr, locationBAqi
                    )
                    navController.navigate(Screen.BookingResult.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(
            Screen.Nickname.route,
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("address") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: NicknameViewModel = hiltViewModel()
            val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
            val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f
            val address = backStackEntry.arguments?.getString("address") ?: ""

            NicknameScreen(
                viewModel = viewModel,
                address = address,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble(),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.BookingResult.route) {
            BookingResultScreen(
                viewModel = bookingViewModel,
                mapViewModel = mapViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route) {
                        popUpTo(Screen.Map.route)
                    }
                }
            )
        }

        composable(Screen.History.route) {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                viewModel = historyViewModel,
                mapViewModel = mapViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
