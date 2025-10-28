package com.example.soulstone.data.model

data class TranslatedBenefit(
    val id: Int,
    val keyName: String,

    // Fields from the BenefitTranslation table
    val translatedName: String,
    val languageCode: LanguageCode
)
