package com.example.soulstone.ui.models

data class StoneUiItem(
    val id: Int,
    val name: String,
    val category: String,
    val zodiacSign: String,
    val chineseZodiacSign: String,
    val chakra: String,
    val description: String,
    val imageResId: Int,
    val isEditing: Boolean,
)
