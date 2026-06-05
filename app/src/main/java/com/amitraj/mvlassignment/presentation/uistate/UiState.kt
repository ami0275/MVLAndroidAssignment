package com.amitraj.mvlassignment.presentation.uistate

import com.amitraj.mvlassignment.domain.model.Location

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class MapUiState(
    val currentLocation: Location? = null,
    val locationA: Location? = null,
    val locationB: Location? = null,
    val currentAqi: Int? = null,
    val aqi: UiState<Int> = UiState.Loading,
    val address: UiState<String> = UiState.Loading,
    val buttonState: ButtonState = ButtonState.SET_A,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class ButtonState {
    SET_A, SET_B, BOOK
}

data class NicknameUiState(
    val address: String = "",
    val nickname: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

data class BookingUiState(
    val bookingState: UiState<BookingResultData> = UiState.Loading,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class BookingResultData(
    val bookId: String,
    val locationA: Location,
    val locationB: Location,
    val price: Int
)

data class HistoryUiState(
    val books: UiState<List<BookingResultData>> = UiState.Loading,
    val totalCount: Int = 0,
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
