package com.amitraj.mvlassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.amitraj.mvlassignment.presentation.navigation.AppNavigation
import com.amitraj.mvlassignment.ui.theme.MvlAssignmentTheme
import com.amitraj.mvlassignment.ui.theme.SkyBlue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(SkyBlue.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(SkyBlue.toArgb())
        )
        setContent {
            MvlAssignmentTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MvlAssignmentTheme {
        // Preview not available for full app with navigation
    }
}