package com.example.soulstone.ui.models

import com.example.soulstone.data.pojos.TranslatedChineseZodiacSign

data class ChineseSignUiDetails(
    val data: TranslatedChineseZodiacSign,
    val imageResId: Int,
    val imageBorderResId: Int,
    val imageColorResId: Int
)
