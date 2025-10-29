package com.example.soulstone.ui.screens.horoscope_monthly_birthstones
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.LocalLanguage
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.domain.model.ZodiacSignDetails
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.util.ZodiacSign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ZodiacUiState(
    val isLoading: Boolean = false,
    val selectedSignDetails: TranslatedZodiacSign? = null
)

@HiltViewModel
class ZodiacViewModel @Inject constructor(
    private val repository: ZodiacSignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZodiacUiState())
    val uiState: StateFlow<ZodiacUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onSignSelected(sign: ZodiacSign, language: LanguageCode) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val details = repository.getTranslatedZodiacSign(sign.signName, language)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    selectedSignDetails = details
                )
            }
            _navigationEvent.emit(sign.name)
        }
    }
}