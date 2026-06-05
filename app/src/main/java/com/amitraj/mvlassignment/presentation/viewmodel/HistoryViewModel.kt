package com.amitraj.mvlassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitraj.mvlassignment.domain.repository.BookingRepository
import com.amitraj.mvlassignment.presentation.uistate.BookingResultData
import com.amitraj.mvlassignment.presentation.uistate.HistoryUiState
import com.amitraj.mvlassignment.presentation.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, books = UiState.Loading) }
            
            val now = LocalDate.now()
            bookingRepository.getBooksByMonthYear(now.year, now.monthValue)
                .onSuccess { books ->
                    val resultData = books.map { book ->
                        BookingResultData(
                            bookId = book.id,
                            locationA = book.locationA,
                            locationB = book.locationB,
                            price = book.price
                        )
                    }
                    val totalPrice = resultData.sumOf { it.price }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            books = UiState.Success(resultData),
                            totalCount = resultData.size,
                            totalPrice = totalPrice
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, books = UiState.Error(error.message ?: "Unknown error")) }
                }
        }
    }
}
