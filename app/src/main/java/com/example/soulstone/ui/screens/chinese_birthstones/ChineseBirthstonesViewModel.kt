package com.example.soulstone.ui.screens.chinese_birthstones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.ChineseZodiacSignEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class ChineseZodiacUiState(
//    val isLoading: Boolean = false
//)

@HiltViewModel
class ChineseBirthstonesViewModel @Inject constructor(
): ViewModel() {

//    private val _uiState = MutableStateFlow(ChineseZodiacUiState())
//    val uiState: StateFlow<ChineseZodiacUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSignClicked(sign: ChineseZodiacSignEnum) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToChineseSign(sign.signName))
        }
    }
}