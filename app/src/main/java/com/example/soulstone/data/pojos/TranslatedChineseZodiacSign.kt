package com.example.soulstone.data.pojos

import androidx.room.ColumnInfo
import com.example.soulstone.util.LanguageCode

data class TranslatedChineseZodiacSign(
    val recentYears: String,
    val iconName: String,
    val iconBorderName: String,
    val iconColorName: String,

    val name: String,
    val description: String,
    val traits: String,
    val bestMatch: String,
    val worstMatch: String,
    val compatibilityDescription: String,
    val gemstoneDescription: String
)
