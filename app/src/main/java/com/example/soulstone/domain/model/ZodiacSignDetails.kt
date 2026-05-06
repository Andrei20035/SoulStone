package com.example.soulstone.domain.model

import androidx.annotation.DrawableRes
import com.example.soulstone.R

data class ZodiacSignDetails(
    val name: String,
    val startDate: String,
    val endDate: String,
    val element: String,
    @DrawableRes val iconResId: Int
)

val zodiacSigns = listOf(
    ZodiacSignDetails("Aries", "March 21", "April 19", "Fire", R.drawable.aries),
    ZodiacSignDetails("Taurus", "April 20", "May 20", "Earth", R.drawable.taurus),
    ZodiacSignDetails("Gemini", "May 21", "June 20", "Air", R.drawable.gemini),
    ZodiacSignDetails("Cancer", "June 21", "July 22", "Water", R.drawable.cancer),
    ZodiacSignDetails("Leo", "July 23", "August 22", "Fire", R.drawable.leo),
    ZodiacSignDetails("Virgo", "August 23", "September 22", "Earth", R.drawable.virgo),
    ZodiacSignDetails("Libra", "September 23", "October 22", "Air", R.drawable.libra),
    ZodiacSignDetails("Scorpio", "October 23", "November 21", "Water", R.drawable.scorpio),
    ZodiacSignDetails("Sagittarius", "November 22", "December 21", "Fire", R.drawable.sagittarius),
    ZodiacSignDetails("Capricorn", "December 22", "January 19", "Earth", R.drawable.capricorn),
    ZodiacSignDetails("Aquarius", "January 20", "February 18", "Air", R.drawable.aquarius),
    ZodiacSignDetails("Pisces", "February 19", "March 20", "Water", R.drawable.pisces)
)
