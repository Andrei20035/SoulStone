package com.example.soulstone.data.repository

import com.example.soulstone.util.LanguageCode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    /**
     * A flow that emits the currently selected language.
     * It will emit the saved value immediately upon collection and
     * any subsequent changes.
     */
    val language: Flow<LanguageCode>

    /**
     * Saves the selected language to persistent storage.
     *
     * @param language The language code or name to save (e.g., "English").
     */
    suspend fun saveLanguage(language: LanguageCode)
}