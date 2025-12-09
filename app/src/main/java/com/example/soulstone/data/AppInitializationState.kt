package com.example.soulstone.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializationState @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val isSetupDone = sharedPreferences.getBoolean("is_db_setup", false)

    private val _isDatabaseReady = MutableStateFlow(isSetupDone)
    val isDatabaseReady = _isDatabaseReady.asStateFlow()

    fun markDatabaseAsReady() {
        sharedPreferences.edit().putBoolean("is_db_setup", true).apply()
        _isDatabaseReady.value = true
    }
}