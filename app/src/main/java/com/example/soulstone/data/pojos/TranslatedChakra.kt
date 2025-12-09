package com.example.soulstone.data.pojos

data class TranslatedChakra(
    // From Parent (chakras table)
    val id: Int,
    val sanskritName: String,
    val imageName: String,

    // From Child (chakra_translations table)
    val name: String,
    val description: String,
    val location: String,
    val rulingPlanet: String,
    val element: String,
    val stoneColors: String,
    val healingQualities: String,
    val stones: String,
    val bodyPlacement: String,
    val housePlacement: String,
    val herbs: String,
    val essentialOils: String
)