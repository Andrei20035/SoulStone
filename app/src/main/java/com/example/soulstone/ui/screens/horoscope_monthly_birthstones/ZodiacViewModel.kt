package com.example.soulstone.ui.screens.horoscope_monthly_birthstones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.ZodiacSignEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ZodiacUiState(
    val isLoading: Boolean = false
)

@HiltViewModel
class ZodiacViewModel @Inject constructor(
    private val repository: ZodiacSignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZodiacUiState())
    val uiState: StateFlow<ZodiacUiState> = _uiState.asStateFlow()

    // 1. Replaced StateFlow<String?> with SharedFlow<UiEvent>
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        /*
        viewModelScope.launch(Dispatchers.IO) {
            repository.dbInitialization()
        }
        */
    }

    fun onSignClicked(sign: ZodiacSignEnum) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToZodiacSign(sign.signName))
        }
    }
}