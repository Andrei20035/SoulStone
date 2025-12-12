package com.example.soulstone.ui.screens.horoscope_sign_details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.StoneListUiItem
import com.example.soulstone.ui.models.ZodiacSignUiDetails
import com.example.soulstone.ui.models.ZodiacSignListUiItem
import com.example.soulstone.util.getDrawableIdByName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val sign: ZodiacSignUiDetails? = null,
    val associatedStones: List<StoneListUiItem> = emptyList(),
    val allSigns: List<ZodiacSignListUiItem> = emptyList(),
)

@HiltViewModel
class HoroscopeSignDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val zodiacRepository: ZodiacSignRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
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

                        val mappedSign = sign?.let { safeSign ->
                            ZodiacSignUiDetails(
                                data = safeSign,
                                imageResId = context.getDrawableIdByName(safeSign.imageName)
                            )
                        }

                        val mappedStones = stones.map { dbItem ->
                            StoneListUiItem(
                                id = dbItem.id,
                                name = dbItem.name,
                                imageResId = context.getDrawableIdByName(dbItem.imageUri)
                            )
                        }

                        val mappedAllSigns = allSigns.map { dbItem ->
                            ZodiacSignListUiItem(
                                id = dbItem.id,
                                signName = dbItem.signName,
                                keyName = dbItem.keyName,
                                imageResId = context.getDrawableIdByName(dbItem.imageName)
                            )
                        }

                        Triple(mappedSign, mappedStones, mappedAllSigns)
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