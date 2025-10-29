package com.example.soulstone.data.pojos

import com.example.soulstone.util.LanguageCode

data class TranslatedZodiacSign(
    val id: Int,
    val keyName: String,
    val startDate: String,
    val endDate: String,

    val translatedName: String,
    val description: String,
    val element: String,
    val rulingPlanet: String,
    val languageCode: LanguageCode
)
