package com.example.soulstone

import androidx.compose.runtime.compositionLocalOf
import com.example.soulstone.util.LanguageCode

/**
 * A CompositionLocal to provide the current LanguageCode down the composable tree.
 * We provide a default value (ENGLISH) which is useful for previews.
 */
val LocalLanguage = compositionLocalOf { LanguageCode.ENGLISH }