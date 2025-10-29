package com.example.soulstone.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.util.LanguageCode
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

    fun onLanguageSelected(language: LanguageCode) {
        viewModelScope.launch {
             settingsRepository.saveLanguage(language)
        }
    }
}