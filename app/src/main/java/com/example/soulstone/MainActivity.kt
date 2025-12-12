package com.example.soulstone

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.soulstone.data.AppInitializationState
import com.example.soulstone.data.database.AppDatabase
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.ui.screens.LoadingScreen
import com.example.soulstone.ui.theme.SoulStoneTheme
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import com.example.soulstone.util.InactivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

        splashScreen.setKeepOnScreenCondition {
            false
        }
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                appDatabase.openHelper.writableDatabase
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setContent {
            val language by settingsRepository.language.collectAsState(initial = LanguageCode.ENGLISH)

            val isDatabaseReady by appInitState.isDatabaseReady.collectAsState(initial = false)

            LaunchedEffect(language) {
                val appLocale = LocaleListCompat.forLanguageTags(language.code)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

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