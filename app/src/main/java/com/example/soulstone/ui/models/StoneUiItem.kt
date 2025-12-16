package com.example.soulstone.ui.models

data class StoneUiItem(
    val id: Int,
    val name: String,
    val category: List<String>,
    val zodiacSign: List<String>,
    val chineseZodiacSign: List<String>,
    val chakra: List<String>,
    val description: String,
    val imageResId: Int,
    val imageFileName: String? = null,
    val isEditing: Boolean,
)
