package com.example.soulstone

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.soulstone.data.AppInitializationState
import com.example.soulstone.data.database.AppDatabase
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.ui.screens.LoadingScreen
import com.example.soulstone.ui.theme.SoulStoneTheme
import com.example.soulstone.util.InactivityManager
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var inactivityManager: InactivityManager

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var appInitState: AppInitializationState

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        splashScreen.setKeepOnScreenCondition { false }
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                appDatabase.openHelper.writableDatabase
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        lifecycleScope.launch {
            settingsRepository.language
                .distinctUntilChanged()
                .collect { language ->
                    val currentLocales = AppCompatDelegate.getApplicationLocales()
                    val currentTag = currentLocales.get(0)?.language ?: ""

                    if (currentTag != language.code && language.code.isNotEmpty()) {
                        val appLocale = LocaleListCompat.forLanguageTags(language.code)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                }
        }

        setContent {
            val language by settingsRepository.language.collectAsState(initial = LanguageCode.ENGLISH)
            val isDatabaseReady by appInitState.isDatabaseReady.collectAsState(initial = false)

            CompositionLocalProvider(LocalLanguage provides language) {
                SoulStoneTheme {
                    if (isDatabaseReady) {
                        SoulStoneAppUI(inactivityManager)
                    } else {
                        LoadingScreen()
                    }
                }
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        inactivityManager.onUserInteraction()
    }
}