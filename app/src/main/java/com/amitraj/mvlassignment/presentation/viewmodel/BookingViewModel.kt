package com.amitraj.mvlassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitraj.mvlassignment.domain.repository.BookingRepository
import com.amitraj.mvlassignment.presentation.uistate.BookingResultData
import com.amitraj.mvlassignment.presentation.uistate.BookingUiState
import com.amitraj.mvlassignment.presentation.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun createBook(
        locationALat: Double,
        locationALng: Double,
        locationAAddress: String,
        locationAAqi: Int?,
        locationBLat: Double,
        locationBLng: Double,
        locationBAddress: String,
        locationBAqi: Int?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, bookingState = UiState.Loading) }
            
            bookingRepository.createBook(
                locationALat,
                locationALng,
                locationAAddress,
                locationAAqi,
                locationBLat,
                locationBLng,
                locationBAddress,
                locationBAqi
            )
                .onSuccess { book ->
                    val resultData = BookingResultData(
                        bookId = book.id,
                        locationA = book.locationA.copy(address = locationAAddress),
                        locationB = book.locationB.copy(address = locationBAddress),
                        price = book.price
                    )
                    _uiState.update { it.copy(isLoading = false, bookingState = UiState.Success(resultData)) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, bookingState = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }
}
