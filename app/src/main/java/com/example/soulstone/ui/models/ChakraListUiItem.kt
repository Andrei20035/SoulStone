package com.example.soulstone.ui.models

data class ChakraListUiItem(
    val id: Int,
    val imageResId: Int = 0,
    val imageFileName: String? = null,
    val sanskritName: String,
    val chakraName: String,
)
