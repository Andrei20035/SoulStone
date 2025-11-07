package com.example.soulstone.ui.events

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateToBenefit(val benefitId: Int) : UiEvent()
    data class NavigateToStoneDetail(val stoneId: Int) : UiEvent()
    data class NavigateToChakraDetails(val chakraId: Int) : UiEvent()
}