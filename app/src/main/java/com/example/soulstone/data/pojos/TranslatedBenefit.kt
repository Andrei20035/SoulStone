package com.example.soulstone.data.pojos

import androidx.annotation.DrawableRes
import com.example.soulstone.util.LanguageCode

data class TranslatedBenefit(
    val id: Int,
    val keyName: String,
    @DrawableRes val imageResId: Int,

    val translatedName: String,
    val languageCode: LanguageCode
)
