package com.example.soulstone.ui.screens.stone_uses

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.pojos.TranslatedBenefit
import com.example.soulstone.data.repository.BenefitRepository
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.ui.models.BenefitUiItem
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoneUsesUiState(
    val isLoading: Boolean = false,
    val benefits: List<BenefitUiItem> = emptyList()
)

@HiltViewModel
class StoneUsesViewModel @Inject constructor(
    private val repository: BenefitRepository,
    private val settings: SettingsRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState = MutableStateFlow(StoneUsesUiState())
    val uiState: StateFlow<StoneUsesUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchBenefitsOnLanguageChange()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchBenefitsOnLanguageChange() {
        viewModelScope.launch {
            settings.language
                .flatMapLatest { language ->
                    _uiState.update { it.copy(isLoading = true) }

                    repository.getAllTranslatedBenefits(language)
                        .map { list ->
                            list.map { benefit ->
                                BenefitUiItem(
                                    id = benefit.id,
                                    name = benefit.translatedName,
                                    imageName = benefit.imageName,
                                    imageResId = context.getDrawableIdByName(benefit.imageName)
                                )
                            }
                        }
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message}"))
                }
                .collect { benefitsList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            benefits = benefitsList
                        )
                    }
                }
        }
    }

    fun onBenefitClicked(benefitId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToBenefit(benefitId))
        }
    }
}