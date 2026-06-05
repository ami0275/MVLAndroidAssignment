package com.amitraj.mvlassignment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Circular menu segment for radial menu navigation
 *
 * @param label Text label for the segment
 * @param icon Icon to display in the segment
 * @param angle Angle in degrees (0-360) where segment is positioned
 * @param radius Distance from center in dp
 * @param modifier Optional modifier for customization
 * @param isSelected Whether this segment is currently selected
 * @param segmentColor Color for this segment
 * @param onClick Callback when segment is clicked
 */
@Composable
fun CircularMenuSegment(
    label: String,
    icon: ImageVector,
    angle: Float,
    radius: Float,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    segmentColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit = {},
) {
    // Convert angle to radians and calculate offset
    val angleRad = Math.toRadians(angle.toDouble())
    val offsetX = (radius * cos(angleRad)).toFloat().dp
    val offsetY = (radius * sin(angleRad)).toFloat().dp

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    segmentColor,
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .size(64.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
