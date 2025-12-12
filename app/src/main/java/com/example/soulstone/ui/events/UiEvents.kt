package com.example.soulstone.ui.events

sealed class UiEvent {
    // --- Existing Events with Data ---
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateToBenefit(val benefitId: Int) : UiEvent()
    data class NavigateToStoneDetail(val stoneId: Int) : UiEvent()
    data class NavigateToChakraDetails(val keyName: String) : UiEvent()
    data class NavigateToZodiacSign(val keyName: String) : UiEvent()
    data class NavigateToChineseSign(val keyName: String) : UiEvent()

    // --- Navigation Events (No Parameters) ---
    object NavigateBack : UiEvent()

    // New Top Bar Navigation Events
    object NavigateHome : UiEvent()
    object NavigateChineseBirthstones : UiEvent()
    object NavigateStoneUses : UiEvent()
    object NavigateSevenChakras : UiEvent()
    object NavigateAdmin : UiEvent()
    object NavigateGemstoneIndex : UiEvent()

    // -- Admin navigation events
    object NavigateToAdminDashboard : UiEvent()
    object NavigateToAddStone : UiEvent()
}