package com.amitraj.mvlassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amitraj.mvlassignment.ui.components.AppTopBar
import com.amitraj.mvlassignment.ui.components.ListItemRow

/**
 * Browse List Screen
 *
 * Displays a list of items with category or filtered view
 */
@Composable
fun BrowseListScreen(
    title: String = "Browse",
    onBackClick: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
) {
    // Sample data
    val listItems = listOf(
        Pair("Popular Place 1", "2.5 km away • 4.8★"),
        Pair("Popular Place 2", "1.2 km away • 4.6★"),
        Pair("Popular Place 3", "3.1 km away • 4.9★"),
        Pair("Popular Place 4", "0.8 km away • 4.7★"),
        Pair("Popular Place 5", "2.9 km away • 4.5★"),
        Pair("Popular Place 6", "1.5 km away • 4.8★"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Top App Bar
        AppTopBar(
            title = title,
            onBackClick = onBackClick,
        )

        // List of items
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = listItems,
                key = { it.first },
            ) { (itemTitle, itemSubtitle) ->
                ListItemRow(
                    title = itemTitle,
                    subtitle = itemSubtitle,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Storefront,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailing = {
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Navigate",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    onClick = { onItemClick(itemTitle) },
                )
            }
        }
    }
}
