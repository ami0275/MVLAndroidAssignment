package com.amitraj.mvlassignment.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitraj.mvlassignment.presentation.uistate.UiState
import com.amitraj.mvlassignment.presentation.viewmodel.BookingViewModel
import com.amitraj.mvlassignment.ui.theme.*
import com.amitraj.mvlassignment.presentation.viewmodel.MapViewModel

@Composable
fun BookingResultScreen(
    viewModel: BookingViewModel,
    mapViewModel: MapViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        mapViewModel.resetState()
        onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = com.amitraj.mvlassignment.R.string.title_booking_confirmation), fontSize = 24.sp)

        when (val bookingState = uiState.bookingState) {
            is UiState.Loading -> {
                Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_loading), modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.Success -> {
                val result = bookingState.data

                // Location A
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightBlue)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_location_a), fontSize = 16.sp)
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_address, result.locationA.address), fontSize = 12.sp)
                    Text(
                        stringResource(id = com.amitraj.mvlassignment.R.string.label_coordinates, result.locationA.latitude.toString(), result.locationA.longitude.toString()),
                        fontSize = 12.sp
                    )
                }

                HorizontalDivider()

                // Location B
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightBlue)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_location_b), fontSize = 16.sp)
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_address, result.locationB.address), fontSize = 12.sp)
                    Text(
                        stringResource(id = com.amitraj.mvlassignment.R.string.label_coordinates, result.locationB.latitude.toString(), result.locationB.longitude.toString()),
                        fontSize = 12.sp
                    )
                }

                HorizontalDivider()

                // Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightOrange)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_total_price), fontSize = 16.sp)
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_price_krw, result.price.toString()), fontSize = 18.sp)
                }

                // View History button
                Button(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = CtaGold,
                        contentColor = Charcoal
                    )
                ) {
                    Text(
                        text = stringResource(id = com.amitraj.mvlassignment.R.string.btn_view_history),
                        fontFamily = Pretendard,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp
                    )
                }

                // Back to Map button
                Button(
                    onClick = {
                        mapViewModel.resetState()
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.btn_back_to_map), color = Color.Black)
                }
            }
            is UiState.Error -> {
                Text("Error: ${bookingState.message}", color = Color.Red)
            }
        }
    }
}
