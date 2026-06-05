package com.amitraj.mvlassignment.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amitraj.mvlassignment.ui.screens.BrowseListScreen
import com.amitraj.mvlassignment.ui.screens.CircularMenuScreen
import com.amitraj.mvlassignment.ui.screens.LocationScreen
import com.amitraj.mvlassignment.ui.screens.SettingsScreen

/**
 * Screen navigation states
 */
sealed class Screen {
    object CircularMenu : Screen()
    object BrowseList : Screen()
    data class Location(val itemName: String = "Sample Location") : Screen()
    object Settings : Screen()
}

/**
 * Main App Navigation
 *
 * Manages navigation between all screens and handles state transitions
 */
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CircularMenu) }

    when (currentScreen) {
        is Screen.CircularMenu -> {
            CircularMenuScreen(
                onSegmentClick = { label ->
                    currentScreen = Screen.BrowseList
                },
                onSettingsClick = {
                    currentScreen = Screen.Settings
                },
            )
        }

        is Screen.BrowseList -> {
            BrowseListScreen(
                title = "Browse",
                onBackClick = {
                    currentScreen = Screen.CircularMenu
                },
                onItemClick = { itemName ->
                    currentScreen = Screen.Location(itemName)
                },
            )
        }

        is Screen.Location -> {
            LocationScreen(
                title = (currentScreen as Screen.Location).itemName,
                onBackClick = {
                    currentScreen = Screen.BrowseList
                },
            )
        }

        is Screen.Settings -> {
            SettingsScreen(
                onBackClick = {
                    currentScreen = Screen.CircularMenu
                },
                onSettingsSave = { notifications, theme ->
                    // Handle settings save here
                    currentScreen = Screen.CircularMenu
                },
            )
        }
    }
}
