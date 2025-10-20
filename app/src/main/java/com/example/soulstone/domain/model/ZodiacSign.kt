package com.example.soulstone.domain.model

import androidx.annotation.DrawableRes
import com.example.soulstone.R

data class ZodiacSign(
    val name: String,
    val startDate: String,
    val endDate: String,
    val element: String,
    @DrawableRes val iconResId: Int
)

val zodiacSigns = listOf(
    ZodiacSign("Aries", "March 21", "April 19", "Fire", R.drawable.aries),
    ZodiacSign("Taurus", "April 20", "May 20", "Earth", R.drawable.taurus),
    ZodiacSign("Gemini", "May 21", "June 20", "Air", R.drawable.gemini),
    ZodiacSign("Cancer", "June 21", "July 22", "Water", R.drawable.cancer),
    ZodiacSign("Leo", "July 23", "August 22", "Fire", R.drawable.leo),
    ZodiacSign("Virgo", "August 23", "September 22", "Earth", R.drawable.virgo),
    ZodiacSign("Libra", "September 23", "October 22", "Air", R.drawable.libra),
    ZodiacSign("Scorpio", "October 23", "November 21", "Water", R.drawable.scorpio),
    ZodiacSign("Sagittarius", "November 22", "December 21", "Fire", R.drawable.sagittarius),
    ZodiacSign("Capricorn", "December 22", "January 19", "Earth", R.drawable.capricorn),
    ZodiacSign("Aquarius", "January 20", "February 18", "Air", R.drawable.aquarius),
    ZodiacSign("Pisces", "February 19", "March 20", "Water", R.drawable.pisces)
)
