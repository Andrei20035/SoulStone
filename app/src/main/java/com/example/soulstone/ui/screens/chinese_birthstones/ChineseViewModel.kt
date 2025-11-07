package com.example.soulstone.ui.screens.chinese_birthstones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.ChineseZodiacSignRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.screens.horoscope_monthly_birthstones.ZodiacUiState
import com.example.soulstone.util.ChineseZodiacSign
import com.example.soulstone.util.ZodiacSign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChineseZodiacUiState(
    val isLoading: Boolean = false
)

@HiltViewModel
class ChineseViewModel @Inject constructor(
    private val repository: ChineseZodiacSignRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ChineseZodiacUiState())
    val uiState: StateFlow<ChineseZodiacUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<Int?>(null)
    val navigationEvent: StateFlow<Int?> = _navigationEvent.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSignClicked(sign: ChineseZodiacSign) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val signEntity = repository.getChineseSignByName(sign.signName)

                if (signEntity != null) {
                    _navigationEvent.value = signEntity.id
                } else {
                    _uiEvent.emit(UiEvent.ShowSnackbar("Sign not found. Please try again."))
                }
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message ?: "Unknown error"}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
}