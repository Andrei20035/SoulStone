package com.example.soulstone.data.pojos

import com.example.soulstone.util.LanguageCode

data class TranslatedStone(
    val id: Int,
    val imageUri: String?,

    val translatedName: String,
    val description: String,
    val languageCode: LanguageCode
)
