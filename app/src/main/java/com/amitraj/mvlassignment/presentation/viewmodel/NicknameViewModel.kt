package com.amitraj.mvlassignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitraj.mvlassignment.domain.repository.LocationRepository
import com.amitraj.mvlassignment.presentation.uistate.NicknameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NicknameUiState())
    val uiState: StateFlow<NicknameUiState> = _uiState.asStateFlow()

    fun initializeWithAddress(address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val nickname = locationRepository.getLocationNickname(latitude, longitude) ?: ""
            _uiState.update { 
                it.copy(
                    address = address,
                    nickname = nickname
                ) 
            }
        }
    }

    fun updateNickname(nickname: String) {
        if (nickname.length <= 20) {
            _uiState.update { it.copy(nickname = nickname) }
        }
    }

    fun saveNickname(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            try {
                locationRepository.saveLocationNickname(latitude, longitude, _uiState.value.nickname)
                _uiState.update { it.copy(isSaving = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }
}
