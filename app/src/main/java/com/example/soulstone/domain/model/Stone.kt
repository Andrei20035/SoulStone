package com.example.soulstone.domain.model

import android.net.Uri

data class Stone(
    val id: Int,
    val name: String,
    val image:  Uri,
    val description: String,
    val zodiacSign: ZodiacSign,
    val chineseZodiacSign: ChineseZodiacSign,
    val benefits: List<StoneBenefit>,
    val chakras: List<Chakra>
)
