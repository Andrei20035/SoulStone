package com.example.soulstone.ui.screens.horoscope_sign_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import com.example.soulstone.data.pojos.ZodiacSignListItem
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignDetailsUiState(
    val isLoading: Boolean = false,
    val sign: TranslatedZodiacSign? = null,
    val associatedStones: List<StoneListItem> = emptyList(),
    val allSigns: List<ZodiacSignListItem> = emptyList(),
    val userMessage: String? = null
)

@HiltViewModel
class HoroscopeSignDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val zodiacRepository: ZodiacSignRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val signName: String = checkNotNull(savedStateHandle["signName"])

    private val _uiState = MutableStateFlow(SignDetailsUiState(isLoading = true))
    val uiState: StateFlow<SignDetailsUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeContent()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeContent() {
        viewModelScope.launch {
            settingsRepository.language
                .flatMapLatest { languageCode ->
                    val signFlow = zodiacRepository.getTranslatedZodiacSignFlow(signName, languageCode)
                    val stonesFlow = zodiacRepository.getStonesForSignFlow(signName, languageCode, limit = 8)
                    val allSignsFlow = zodiacRepository.getZodiacSignListItems(languageCode)

                    combine(signFlow, stonesFlow, allSignsFlow) { sign, stones, allSigns ->
                        Triple(sign, stones, allSigns)
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message ?: "Unknown error"}"))
                }
                .collect { (signData, stonesData, allSignsList) ->
                    if (signData != null) {
                        _uiState.update { it.copy(
                            isLoading = false,
                            sign = signData,
                            associatedStones = stonesData,
                            allSigns = allSignsList
                        )}
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(UiEvent.ShowSnackbar("Sign details not found."))
                    }
                }
        }
    }

    fun onStoneClicked(stoneId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToStoneDetail(stoneId))
        }
    }

    fun onSignClicked(keyName: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToZodiacSign(keyName))
        }
    }
}