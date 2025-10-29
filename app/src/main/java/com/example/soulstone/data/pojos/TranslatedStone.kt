package com.example.soulstone.data.pojos

import com.example.soulstone.util.LanguageCode

data class TranslatedStone(
    // Fields from the main Stone table
    val id: Int,
    val keyName: String,
    val imageUri: String?,

    // Fields from the StoneTranslation table
    val translatedName: String,
    val description: String,
    val languageCode: LanguageCode
)
