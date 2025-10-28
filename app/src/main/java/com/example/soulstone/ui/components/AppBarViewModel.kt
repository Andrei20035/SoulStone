package com.example.soulstone.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AppBarUiState())
    val uiState: StateFlow<AppBarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
             settingsRepository.language.collect { savedLanguage ->
                 _uiState.update { it.copy(selectedLanguage = savedLanguage) }
             }
        }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }

        viewModelScope.launch {
             settingsRepository.saveLanguage(language)
            // TODO: Add logic here to change the app's locale
        }
    }
}