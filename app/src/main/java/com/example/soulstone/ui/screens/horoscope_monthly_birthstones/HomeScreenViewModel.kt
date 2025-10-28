package com.example.soulstone.ui.screens.horoscope_monthly_birthstones
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.domain.model.ZodiacSignDetails
import com.example.soulstone.domain.model.ZodiacSignEnum
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
    val selectedSignDetails: ZodiacSignDetails? = null
)

@HiltViewModel
class ZodiacViewModel @Inject constructor(
    private val repository: ZodiacRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZodiacUiState())
    val uiState: StateFlow<ZodiacUiState> = _uiState.asStateFlow()

    // A one-time event flow to tell the UI to navigate.
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * This is called by the UI when a sign is clicked.
     */
    fun onSignSelected(sign: ZodiacSignEnum) {
        viewModelScope.launch {
            // 1. Set loading state
            _uiState.update { it.copy(isLoading = true) }

            // 2. Fetch the data from the repository
            val details = repository.getSignDetails(sign)

            // 3. Update the state with the new details
            _uiState.update {
                it.copy(
                    isLoading = false,
                    selectedSignDetails = details
                )
            }

            // 4. Send a one-time event to trigger navigation
            // We pass the route, which we get from the enum.
            _navigationEvent.emit(sign.route)
        }
    }
}