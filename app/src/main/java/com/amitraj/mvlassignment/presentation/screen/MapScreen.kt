package com.amitraj.mvlassignment.presentation.screen

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitraj.mvlassignment.presentation.viewmodel.MapViewModel
import com.amitraj.mvlassignment.presentation.uistate.ButtonState
import com.amitraj.mvlassignment.presentation.uistate.MapUiState
import com.amitraj.mvlassignment.presentation.uistate.UiState
import com.google.android.gms.maps.model.CameraPosition
import com.amitraj.mvlassignment.ui.theme.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.abs
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import androidx.core.location.LocationManagerCompat

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    onNavigateToNickname: (Double, Double, String) -> Unit,
    onNavigateToBooking: (Double, Double, String, Int?, Double, Double, String, Int?) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.4979, 127.0276), 15f)
    }

    val topPadding = maxOf(WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), 47.dp)

    LaunchedEffect(Unit) {
        viewModel.refreshNicknames()
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            getUserLocation(context) { location ->
                if (location != null) {
                    viewModel.setCurrentLocation(location.latitude, location.longitude)
                } else if (uiState.currentLocation == null) {
                    viewModel.setCurrentLocation(37.4979, 127.0276)
                }
            }
        } else {
            if (uiState.currentLocation == null) {
                viewModel.setCurrentLocation(37.4979, 127.0276)
            }
        }
    }

    // Sync camera position when currentLocation is programmatically updated (e.g. loading a booking)
    LaunchedEffect(uiState.currentLocation) {
        val loc = uiState.currentLocation
        if (loc != null) {
            val currentLatLng = LatLng(loc.latitude, loc.longitude)
            if (cameraPositionState.position.target != currentLatLng) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
            }
        }
    }

    // Track camera movement to update current center location
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val target = cameraPositionState.position.target
            val current = uiState.currentLocation
            if (current == null || 
                abs(current.latitude - target.latitude) > 0.00001 ||
                abs(current.longitude - target.longitude) > 0.00001) {
                viewModel.setCurrentLocation(target.latitude, target.longitude)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Map area
            MapArea(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Bottom section
            BottomSection(
                uiState = uiState,
                onSetA = {
                    val location = uiState.currentLocation
                    if (location != null) {
                        viewModel.setLocationA(location.latitude, location.longitude, location.address)
                    }
                },
                onSetB = {
                    val location = uiState.currentLocation
                    if (location != null) {
                        viewModel.setLocationB(location.latitude, location.longitude, location.address)
                    }
                },
                onBook = {
                    val locA = uiState.locationA
                    val locB = uiState.locationB
                    if (locA != null && locB != null) {
                        val addrA = if (!locA.nickname.isNullOrBlank()) locA.nickname else locA.address
                        val addrB = if (!locB.nickname.isNullOrBlank()) locB.nickname else locB.address
                        onNavigateToBooking(
                            locA.latitude, locA.longitude, addrA, locA.aqi,
                            locB.latitude, locB.longitude, addrB, locB.aqi
                        )
                    }
                },
                onLocationAClick = {
                    val location = uiState.locationA
                    if (location != null) {
                        onNavigateToNickname(
                            location.latitude, location.longitude, location.address
                        )
                    }
                },
                onLocationBClick = {
                    val location = uiState.locationB
                    if (location != null) {
                        onNavigateToNickname(
                            location.latitude, location.longitude, location.address
                        )
                    }
                }
            )
        }

        // Top bar overlay with History Button & AQI Badge
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(top = topPadding, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNavigateToHistory,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(stringResource(id = com.amitraj.mvlassignment.R.string.btn_history), color = Color.White, fontSize = 14.sp)
            }

            AqiBadge(aqi = uiState.currentAqi)
        }
    }
}

@Composable
fun MapArea(
    cameraPositionState: com.google.maps.android.compose.CameraPositionState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(LightGreen)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )
        
        // Center marker
        Image(
            painter = painterResource(id = com.amitraj.mvlassignment.R.drawable.icon_pin),
            contentDescription = stringResource(id = com.amitraj.mvlassignment.R.string.desc_camera_position_pin),
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 32.dp, height = 54.dp)
                .offset(y = (-27).dp)
        )
    }
}

@Composable
fun AqiBadge(aqi: Int?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(
                id = com.amitraj.mvlassignment.R.string.label_aqi,
                aqi?.toString() ?: stringResource(id = com.amitraj.mvlassignment.R.string.aqi_fallback)
            ),
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BottomSection(
    uiState: MapUiState,
    onSetA: () -> Unit,
    onSetB: () -> Unit,
    onBook: () -> Unit,
    onLocationAClick: () -> Unit,
    onLocationBClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomPadding = maxOf(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(), 34.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = bottomPadding)
            .padding(16.dp)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Location A
            LocationRow(
                label = "A",
                address = uiState.locationA?.let {
                    if (!it.nickname.isNullOrBlank()) it.nickname else it.address
                } ?: stringResource(id = com.amitraj.mvlassignment.R.string.address_fallback),
                isSet = uiState.locationA != null,
                onClick = onLocationAClick
            )

            // Location B
            LocationRow(
                label = "B",
                address = uiState.locationB?.let {
                    if (!it.nickname.isNullOrBlank()) it.nickname else it.address
                } ?: stringResource(id = com.amitraj.mvlassignment.R.string.address_fallback),
                isSet = uiState.locationB != null,
                onClick = onLocationBClick
            )
        }

        // Action Button V
        Button(
            onClick = {
                when (uiState.buttonState) {
                    ButtonState.SET_A -> onSetA()
                    ButtonState.SET_B -> onSetB()
                    ButtonState.BOOK -> onBook()
                }
            },
            modifier = Modifier
                .fillMaxHeight()
                .width(110.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CtaGold,
                contentColor = Charcoal
            ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = when (uiState.buttonState) {
                    ButtonState.SET_A -> stringResource(id = com.amitraj.mvlassignment.R.string.btn_set_a)
                    ButtonState.SET_B -> stringResource(id = com.amitraj.mvlassignment.R.string.btn_set_b)
                    ButtonState.BOOK -> stringResource(id = com.amitraj.mvlassignment.R.string.btn_book)
                },
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp
            )
        }
    }
}

@Composable
fun LocationRow(
    label: String,
    address: String,
    isSet: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isSet) LightBlue else LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = isSet) { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = address,
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
    }
}

@SuppressLint("MissingPermission")
private fun getUserLocation(context: Context, callback: (Location?) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    try {
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!hasGps && !hasNetwork) {
            callback(null)
            return
        }
        val provider = if (hasGps) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
        
        val lastKnown = locationManager.getLastKnownLocation(provider)
        if (lastKnown != null) {
            callback(lastKnown)
            return
        }
        
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            provider,
            null,
            context.mainExecutor,
            { location -> callback(location) }
        )
    } catch (e: SecurityException) {
        callback(null)
    } catch (e: Exception) {
        callback(null)
    }
}

