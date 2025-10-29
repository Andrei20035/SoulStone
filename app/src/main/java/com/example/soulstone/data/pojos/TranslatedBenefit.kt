package com.example.soulstone.data.pojos

import com.example.soulstone.util.LanguageCode

data class TranslatedBenefit(
    val id: Int,
    val keyName: String,

    // Fields from the BenefitTranslation table
    val translatedName: String,
    val languageCode: LanguageCode
)
