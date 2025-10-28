package com.example.soulstone.data.model

data class TranslatedChakra(
    val id: Int,
    val sanskritName: String,

    // Fields from the ChakraTranslation table
    val name: String,
    val rulingPlanet: String,
    val color: String,
    val location: String,
    val description: String,
    val languageCode: LanguageCode
)
