package com.example.soulstone.ui.screens.seven_chakra_stones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.data.repository.ChakraRepository
import com.example.soulstone.ui.events.UiEvent
import com.example.soulstone.util.ChakraEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SevenChakraViewModel @Inject constructor(
    private val repository: ChakraRepository
) : ViewModel() {

    // This is for one-time events like navigation or snackbars
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    /**
     * Called when the user clicks on a chakra in the wheel.
     */
    fun onChakraClicked(chakra: ChakraEnum) {
        viewModelScope.launch {
            try {
                val sanskritName = chakra.sanskritName

                val chakraFromDb = repository.findChakraBySanskritName(sanskritName)

                if (chakraFromDb != null) {
                    _uiEvent.emit(UiEvent.NavigateToChakraDetails(chakraFromDb.id))
                } else {
                    _uiEvent.emit(UiEvent.ShowSnackbar("Could not find chakra for ${chakra.name}"))
                }
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${e.message ?: "Unknown error"}"))
            }
        }
    }
}