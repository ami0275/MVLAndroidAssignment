package com.amitraj.mvlassignment.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Reusable list item component with icon, title, subtitle, and trailing content
 *
 * @param title Main title text
 * @param modifier Optional modifier for customization
 * @param subtitle Optional subtitle/description text
 * @param icon Composable for the leading icon
 * @param trailing Optional composable for trailing content (e.g., arrow, badge)
 * @param showDivider Whether to show a divider below the item
 * @param onClick Callback when item is clicked
 */
@Composable
fun ListItemRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: @Composable () -> Unit = {},
    trailing: @Composable () -> Unit = {},
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
) {
    val itemHeight = if (subtitle != null) 72.dp else 56.dp

    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            color = MaterialTheme.colorScheme.surface,
        ) {
            Row(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Leading icon
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    icon()
                }

                // Title and subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Trailing content
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    trailing()
                }
            }
        }

        // Optional divider
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(start = 76.dp, end = 16.dp, top = 0.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}
