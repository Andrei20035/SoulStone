package com.example.soulstone.data.pojos

data class TranslatedZodiacSign(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val imageName: String,

    val name: String,
    val description: String,
    val element: String,
    val rulingPlanet: String,
)
