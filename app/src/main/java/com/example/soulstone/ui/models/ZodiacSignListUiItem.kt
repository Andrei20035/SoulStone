package com.example.soulstone.ui.models

data class ZodiacSignListUiItem(
    val id: Int,
    val keyName: String,
    val signName: String,
    val imageFileName: String? = null,
    val imageResId: Int = 0,
)
