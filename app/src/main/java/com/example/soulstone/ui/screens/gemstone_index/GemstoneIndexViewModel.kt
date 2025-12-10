package com.example.soulstone.ui.screens.gemstone_index

import androidx.lifecycle.ViewModel
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.StoneRepository
import javax.inject.Inject

class GemstoneIndexViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val stoneRepository: StoneRepository
): ViewModel() {
}