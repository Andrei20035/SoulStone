package com.example.soulstone.domain.model

import android.net.Uri

data class Stone(
    val id: Int,
    val name: String,
    val image:  Uri,
    val description: String,
    val zodiacSign: List<ZodiacSign>,
    val chineseZodiacSign: List<ChineseZodiacSign>,
    val benefits: List<StoneBenefit>,
    val chakras: List<Chakra>
)
