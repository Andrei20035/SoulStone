package com.example.soulstone.data.pojos

import com.example.soulstone.util.LanguageCode

data class TranslatedChineseZodiacSign(
    // Fields from the main ChineseZodiacSign table
    val id: Int,
    val keyName: String,
    val recentYears: String,

    // Fields from the ChineseZodiacSignTranslation table
    val translatedName: String,
    val description: String,
    val languageCode: LanguageCode
)
