package com.example.soulstone.data.pojos

data class StoneInventoryView(
    val id: Int,
    val imageUri: String?,
    val stoneName: String,
    val benefit: String?,
    val zodiacSign: String?,
    val chineseZodiacSign: String?,
    val chakra: String?,
    val description: String
)