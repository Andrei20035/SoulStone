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
import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.ui.theme.SoulStoneTheme
import com.example.soulstone.util.LanguageCode
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val language by settingsRepository.language.collectAsState(initial = LanguageCode.ENGLISH)

            // 2. ADD THIS LAUNCHEDEFFECT
            // This code will automatically run every time the 'language' value changes
            LaunchedEffect(language) {
                // Create a locale list from the language code (e.g., "en", "es")
                val appLocale = LocaleListCompat.forLanguageTags(language.code)

                AppCompatDelegate.setApplicationLocales(appLocale)
            }

            CompositionLocalProvider(LocalLanguage provides language) {
                SoulStoneTheme {
                    SoulStoneAppUI()
                }
            }
        }
    }
}