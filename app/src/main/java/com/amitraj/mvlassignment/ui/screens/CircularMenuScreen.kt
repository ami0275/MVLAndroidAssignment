package com.amitraj.mvlassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amitraj.mvlassignment.ui.components.CircularMenuSegment
import com.amitraj.mvlassignment.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Circular Menu Screen
 *
 * Hub screen with radial menu navigation
 * Segments: S (Shop), K (Wishlist/Kartik?), R (Ratings?), B (Browse?)
 */
@Composable
fun CircularMenuScreen(
    onSegmentClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        // Center circle (hub)
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp),
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = com.amitraj.mvlassignment.R.string.label_hub),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        // Circular Menu Segments positioned around center
        val segments = listOf(
            Triple("S", Icons.Filled.Storefront, MenuBlue), // Blue - Shop
            Triple("K", Icons.Filled.ShoppingCart, MenuMagenta), // Magenta - Cart/Wishlist
            Triple("R", Icons.Filled.Star, MenuGray), // Gray - Ratings
            Triple("B", Icons.Filled.Star, MenuDark), // Dark - Browse
        )

        val radius = 120f // Distance from center
        segments.forEachIndexed { index, (label, icon, color) ->
            val angle = (index * 90f) + 45f // Start at 45°, space by 90°

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = (radius * cos(Math.toRadians(angle.toDouble())).toFloat()).dp,
                        y = (radius * sin(Math.toRadians(angle.toDouble())).toFloat()).dp,
                    ),
            ) {
                CircularMenuSegment(
                    label = label,
                    icon = icon,
                    angle = angle,
                    radius = 0f, // Already positioned via offset
                    segmentColor = color,
                    onClick = { onSegmentClick(label) },
                )
            }
        }

        // Settings FAB
        FloatingActionButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-16).dp),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(id = com.amitraj.mvlassignment.R.string.desc_settings),
            )
        }
    }
}
