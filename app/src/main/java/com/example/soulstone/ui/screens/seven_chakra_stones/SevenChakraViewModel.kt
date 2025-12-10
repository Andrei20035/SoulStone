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
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onChakraClicked(keyName: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToChakraDetails(keyName))
        }
    }
}