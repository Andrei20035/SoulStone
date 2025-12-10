package com.example.soulstone.ui.screens.stones_for_benefit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.TranslatedBenefit
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.repository.BenefitRepository
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StonesForXUiState(
    val isBenefitsLoading: Boolean = false,
    val isStonesLoading: Boolean = false,
    val benefitName: String = "",
    val stones: List<TranslatedStone> = emptyList(),
    val allBenefits: List<TranslatedBenefit> = emptyList()
)

@HiltViewModel
class StonesForXViewModel @Inject constructor(
    private val stoneRepository: StoneRepository,
    private val benefitRepository: BenefitRepository,
    private val settings: SettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val benefitId: Int = savedStateHandle.get<Int>("benefitId") ?: 0

    private val _uiState = MutableStateFlow(StonesForXUiState())
    val uiState: StateFlow<StonesForXUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchDataOnLanguageChange()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchDataOnLanguageChange() {
        viewModelScope.launch {
            settings.language
                .flatMapLatest { language ->
                    val stonesFlow = stoneRepository.getStonesForBenefit(benefitId, language)
                        .catch { e -> _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message}")) }
                        .onStart { _uiState.update { it.copy(isStonesLoading = true) } }

                    val benefitFlow =
                        benefitRepository.getTranslatedBenefitFlowById(benefitId, language)
                            .catch { e ->
                                _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message}"))
                            }

                    val allBenefitsFlow = benefitRepository.getAllTranslatedBenefits(language)
                        .catch { e -> _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message}")) }.
                        onStart { _uiState.update { it.copy(isBenefitsLoading = true) } }


                    combine(stonesFlow, benefitFlow, allBenefitsFlow) { stones, benefitFlow,allBenefits ->
                        val name = benefitFlow?.translatedName ?: "Benefit"

                        StonesForXUiState(
                            isStonesLoading = false,
                            isBenefitsLoading = false,
                            benefitName = "Gemstones for $name",
                            stones = stones,
                            allBenefits = allBenefits
                        )
                    }
                }
                .collect { newState ->
                    _uiState.update { newState }
                }
        }
    }

    fun onBenefitClicked(newBenefitId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToBenefit(newBenefitId))
        }
    }

    fun onStoneClicked(stoneId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToStoneDetail(stoneId))
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateBack)
        }
    }

}
