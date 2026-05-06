package com.example.soulstone.ui.screens.chinese_sign_details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.ChineseSignListItem
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.data.pojos.TranslatedChineseZodiacSign
import com.example.soulstone.data.pojos.ZodiacSignListItem
import com.example.soulstone.data.repository.ChineseZodiacSignRepository
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.ChineseSignUiDetails
import com.example.soulstone.ui.models.StoneListUiItem
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

data class ChineseSignDetailsUiState(
    val isLoading: Boolean = false,
    val sign: ChineseSignUiDetails? = null,
    val associatedStones: List<StoneListUiItem> = emptyList(),
    val allSigns: List<ZodiacSignListUiItem> = emptyList(),
)

@HiltViewModel
class ChineseSignDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chineseRepository: ChineseZodiacSignRepository,
    private val stoneRepository: StoneRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val keyName: String = checkNotNull(savedStateHandle["keyName"])

    private val _uiState = MutableStateFlow(ChineseSignDetailsUiState(isLoading = true))
    val uiState: StateFlow<ChineseSignDetailsUiState> = _uiState.asStateFlow()

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

                    val signFlow = chineseRepository.getTranslatedChineseSignFlow(keyName, languageCode)
                    val stonesFlow = stoneRepository.getStonesForChineseSignFlow(keyName, languageCode, 8)
                    val allSignsFlow = chineseRepository.getAllChineseZodiacSignListItems(languageCode)

                    combine(signFlow, stonesFlow, allSignsFlow) { sign, stones, allSigns ->

                        val mappedSign = sign?.let { safeSign ->
                            ChineseSignUiDetails(
                                data = safeSign,
                                imageResId = context.getDrawableIdByName(safeSign.iconName),
                                imageBorderResId = context.getDrawableIdByName(safeSign.iconBorderName),
                                imageColorResId = context.getDrawableIdByName(safeSign.iconColorName)
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
                                keyName = dbItem.keyName,
                                signName = dbItem.signName,
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
            _uiEvent.emit(UiEvent.NavigateToChineseSign(keyName))
        }
    }
}