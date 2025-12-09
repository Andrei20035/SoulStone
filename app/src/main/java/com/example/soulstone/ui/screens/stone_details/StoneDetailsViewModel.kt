package com.example.soulstone.ui.screens.stone_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.TranslatedBenefit
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.repository.BenefitRepository
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.data.repository.ZodiacSignRepository
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoneDetailsUiState(
    val isLoading: Boolean = false,
    val stone: TranslatedStone? = null,
    val benefits: List<TranslatedBenefit> = emptyList(),
    val userMessage: String? = null
)
@HiltViewModel
class StoneDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stoneRepository: StoneRepository,
    private val settingsRepository: SettingsRepository,
    private val benefitRepository: BenefitRepository
) : ViewModel() {

    private val stoneId: Int = checkNotNull(savedStateHandle["stoneId"])

    private val _uiState = MutableStateFlow(StoneDetailsUiState(isLoading = true))
    val uiState: StateFlow<StoneDetailsUiState> = _uiState.asStateFlow()

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
                    val stoneFlow = stoneRepository.getTranslatedStoneFlow(stoneId, languageCode)

                    val benefitsFlow = benefitRepository.getAllTranslatedBenefits(languageCode)

                    combine(stoneFlow, benefitsFlow) { stone, benefits ->
                        Pair(stone, benefits)
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error: ${e.message ?: "Unknown error"}"
                        )
                    }
                }
                .collect { (stoneData, benefitsData) ->
                    if (stoneData != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                stone = stoneData,
                                benefits = benefitsData
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = "Stone details not found."
                            )
                        }
                    }
                }
        }
    }

    fun onBenefitClicked(benefitId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToBenefit(benefitId))
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateBack)
        }
    }

}