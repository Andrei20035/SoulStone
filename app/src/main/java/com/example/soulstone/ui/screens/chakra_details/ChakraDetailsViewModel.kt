package com.example.soulstone.ui.screens.chakra_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.ChakraListItem
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.data.pojos.TranslatedChakra
import com.example.soulstone.data.repository.ChakraRepository
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChakraDetailsUiState(
    val isLoading: Boolean = false,
    val sign: TranslatedChakra? = null,
    val associatedStones: List<StoneListItem> = emptyList(),
    val allChakras: List<ChakraListItem> = emptyList(),
)

@HiltViewModel
class ChakraDetailsViewModel @Inject constructor(
    private val chakraRepository: ChakraRepository,
    private val stoneRepository: StoneRepository,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val keyName: String = checkNotNull(savedStateHandle["keyName"])

    private val _uiState = MutableStateFlow(ChakraDetailsUiState(isLoading = true))
    val uiState: StateFlow<ChakraDetailsUiState> = _uiState.asStateFlow()

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
                    val chakraFlow = chakraRepository.getTranslatedChakraFlow(keyName, languageCode)
                    val stonesFlow = stoneRepository.getStonesForChakraFlow(keyName, languageCode, limit = 8)
                        .onEach { list ->
                            android.util.Log.d("DebugStones", "Found items: ${list.map { it.name }}")
                        }
                    val allChakrasFlow = chakraRepository.getAllChakraListItems(languageCode)

                    combine(chakraFlow, stonesFlow, allChakrasFlow) { chakra, stones, allChakras ->
                        Triple(chakra, stones, allChakras)
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message ?: "Unknown error"}"))
                }
                .collect { (chakraData, stonesData, allChakraList) ->
                    if (chakraData != null) {
                        _uiState.update { it.copy(
                            isLoading = false,
                            sign = chakraData,
                            associatedStones = stonesData,
                            allChakras = allChakraList
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

    fun onChakraClicked(keyName: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToChakraDetails(keyName))
        }
    }
}