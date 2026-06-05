package com.amitraj.mvlassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitraj.mvlassignment.domain.model.Location
import com.amitraj.mvlassignment.domain.repository.LocationRepository
import com.amitraj.mvlassignment.presentation.uistate.ButtonState
import com.amitraj.mvlassignment.presentation.uistate.MapUiState
import com.amitraj.mvlassignment.presentation.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentLocation = Location(latitude, longitude, "Fetching address..."),
                    currentAqi = null,
                    address = UiState.Loading,
                    aqi = UiState.Loading
                )
            }
            
            val aqiResult = locationRepository.fetchAqi(latitude, longitude)
            val addressResult = locationRepository.reverseGeocode(latitude, longitude)
            val nickname = locationRepository.getLocationNickname(latitude, longitude)
            
            val aqiVal = aqiResult.getOrNull()?.aqi
            val addressVal = addressResult.getOrNull()?.address ?: "Seoul Location"
            
            _uiState.update {
                it.copy(
                    currentLocation = Location(
                        latitude = latitude,
                        longitude = longitude,
                        address = addressVal,
                        aqi = aqiVal,
                        nickname = nickname
                    ),
                    currentAqi = aqiVal,
                    address = UiState.Success(addressVal),
                    aqi = if (aqiVal != null) UiState.Success(aqiVal) else UiState.Error("Failed to fetch AQI")
                )
            }
        }
    }

    fun fetchAqi(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(aqi = UiState.Loading) }
            locationRepository.fetchAqi(latitude, longitude)
                .onSuccess { aqiData ->
                    _uiState.update { it.copy(aqi = UiState.Success(aqiData.aqi)) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(aqi = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    fun fetchAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(address = UiState.Loading) }
            locationRepository.reverseGeocode(latitude, longitude)
                .onSuccess { result ->
                    _uiState.update { it.copy(address = UiState.Success(result.address)) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(address = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }

    fun setLocationA(latitude: Double, longitude: Double, address: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            locationRepository.fetchAqi(latitude, longitude)
                .onSuccess { aqiData ->
                    val nickname = locationRepository.getLocationNickname(latitude, longitude)
                    _uiState.update {
                        it.copy(
                            locationA = Location(
                                latitude,
                                longitude,
                                address,
                                aqiData.aqi,
                                nickname
                            ),
                            buttonState = ButtonState.SET_B,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun setLocationB(latitude: Double, longitude: Double, address: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            locationRepository.fetchAqi(latitude, longitude)
                .onSuccess { aqiData ->
                    val nickname = locationRepository.getLocationNickname(latitude, longitude)
                    _uiState.update {
                        it.copy(
                            locationB = Location(
                                latitude,
                                longitude,
                                address,
                                aqiData.aqi,
                                nickname
                            ),
                            buttonState = ButtonState.BOOK,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun refreshNicknames() {
        viewModelScope.launch {
            val state = _uiState.value
            val locA = state.locationA
            if (locA != null) {
                val nicknameA = locationRepository.getLocationNickname(locA.latitude, locA.longitude)
                if (nicknameA != locA.nickname) {
                    _uiState.update { it.copy(locationA = locA.copy(nickname = nicknameA)) }
                }
            }
            val locB = state.locationB
            if (locB != null) {
                val nicknameB = locationRepository.getLocationNickname(locB.latitude, locB.longitude)
                if (nicknameB != locB.nickname) {
                    _uiState.update { it.copy(locationB = locB.copy(nickname = nicknameB)) }
                }
            }
            val current = state.currentLocation
            if (current != null) {
                val nicknameCurrent = locationRepository.getLocationNickname(current.latitude, current.longitude)
                if (nicknameCurrent != current.nickname) {
                    _uiState.update { it.copy(currentLocation = current.copy(nickname = nicknameCurrent)) }
                }
            }
        }
    }

    fun loadBooking(locationA: Location, locationB: Location) {
        _uiState.update {
            it.copy(
                locationA = locationA,
                locationB = locationB,
                currentLocation = locationA,
                currentAqi = locationA.aqi,
                buttonState = ButtonState.BOOK
            )
        }
    }

    fun resetState() {
        _uiState.value = MapUiState()
    }

    fun getDisplayNameA(): String {
        val state = _uiState.value
        val nickname = state.locationA?.nickname
        return if (!nickname.isNullOrBlank()) nickname else state.locationA?.address ?: "Set A"
    }

    fun getDisplayNameB(): String {
        val state = _uiState.value
        val nickname = state.locationB?.nickname
        return if (!nickname.isNullOrBlank()) nickname else state.locationB?.address ?: "Set B"
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
