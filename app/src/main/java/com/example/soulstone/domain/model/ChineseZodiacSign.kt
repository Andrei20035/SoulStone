package com.example.soulstone.domain.model

import androidx.annotation.DrawableRes
import com.example.soulstone.R

data class ChineseZodiacSign(
    val name: String,
    @DrawableRes val iconResId: Int
)

val chineseZodiacSigns = listOf(
    ChineseZodiacSign("Rat", R.drawable.rat),
    ChineseZodiacSign("Ox", R.drawable.ox),
    ChineseZodiacSign("Tiger", R.drawable.tiger),
    ChineseZodiacSign("Rabbit", R.drawable.rabbit),
    ChineseZodiacSign("Dragon", R.drawable.dragon),
    ChineseZodiacSign("Snake", R.drawable.snake),
    ChineseZodiacSign("Horse", R.drawable.horse),
    ChineseZodiacSign("Goat", R.drawable.goat),
    ChineseZodiacSign("Monkey", R.drawable.monkey),
    ChineseZodiacSign("Rooster", R.drawable.rooster),
    ChineseZodiacSign("Dog", R.drawable.dog),
    ChineseZodiacSign("Pig", R.drawable.pig)
)

