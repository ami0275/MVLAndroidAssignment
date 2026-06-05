package com.amitraj.mvlassignment.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitraj.mvlassignment.presentation.uistate.BookingResultData
import com.amitraj.mvlassignment.presentation.uistate.UiState
import com.amitraj.mvlassignment.presentation.viewmodel.HistoryViewModel
import com.amitraj.mvlassignment.ui.theme.*
import com.amitraj.mvlassignment.presentation.viewmodel.MapViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    mapViewModel: MapViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightBlue)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.title_booking_history), fontSize = 24.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_total_bookings), fontSize = 12.sp)
                    Text(uiState.totalCount.toString(), fontSize = 16.sp)
                }
                Column {
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_total_price), fontSize = 12.sp)
                    Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_price_krw, uiState.totalPrice.toString()), fontSize = 16.sp)
                }
            }
        }

        // Bookings list
        when (val booksState = uiState.books) {
            is UiState.Loading -> {
                Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_loading), modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.Success -> {
                if (booksState.data.isEmpty()) {
                    Text(
                        stringResource(id = com.amitraj.mvlassignment.R.string.label_no_bookings), 
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 32.dp),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(booksState.data) { booking ->
                            BookingItem(
                                booking = booking,
                                onClick = {
                                    mapViewModel.loadBooking(booking.locationA, booking.locationB)
                                    onNavigateBack()
                                }
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Text("Error: ${booksState.message}", color = Color.Red)
            }
        }

        // Back button
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = CtaGold,
                contentColor = Charcoal
            )
        ) {
            Text(
                text = stringResource(id = com.amitraj.mvlassignment.R.string.btn_back_to_map),
                fontFamily = com.amitraj.mvlassignment.ui.theme.Pretendard,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp
            )
        }
    }
}

@Composable
fun BookingItem(
    booking: BookingResultData,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray, shape = MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_location_a), fontSize = 12.sp, color = Color.Gray)
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_location_b), fontSize = 12.sp, color = Color.Gray)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(booking.locationA.address, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text(booking.locationB.address, fontSize = 12.sp, modifier = Modifier.weight(1f))
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_price), fontSize = 12.sp)
            Text(stringResource(id = com.amitraj.mvlassignment.R.string.label_price_krw, booking.price.toString()), fontSize = 12.sp)
        }
    }
}
