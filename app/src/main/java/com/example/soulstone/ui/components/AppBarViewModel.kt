package com.example.soulstone.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppBarUiState(
    val selectedLanguage: LanguageCode = LanguageCode.ENGLISH
)

@HiltViewModel
class AppBarViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onLanguageSelected(language: LanguageCode) {
        viewModelScope.launch {
            settingsRepository.saveLanguage(language)
        }
    }

    fun onHomeClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateHome) }
    }

    fun onChineseClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateChineseBirthstones) }
    }

    fun onStoneUsesClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateStoneUses) }
    }

    fun onChakrasClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateSevenChakras) }
    }

    fun onAdminClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateAdmin) }
    }

    fun onGemstoneIndexClicked() {
        viewModelScope.launch { _uiEvent.emit(UiEvent.NavigateGemstoneIndex) }
    }
}