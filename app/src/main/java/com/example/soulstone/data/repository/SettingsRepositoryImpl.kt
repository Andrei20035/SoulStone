package com.example.soulstone.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val SETTINGS_PREFERENCES_NAME = "soulstone_settings"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)

/**
 * Concrete implementation of [SettingsRepository] that uses Preferences DataStore.
 */
@Singleton // Ensures only one instance of this repository exists
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val SELECTED_LANGUAGE_CODE = stringPreferencesKey("selected_language_code")
    }

    override val language: Flow<LanguageCode> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val code = preferences[PreferencesKeys.SELECTED_LANGUAGE_CODE]
            LanguageCode.fromCode(code)
        }

    override suspend fun saveLanguage(language: LanguageCode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LANGUAGE_CODE] = language.code
        }
    }
}
