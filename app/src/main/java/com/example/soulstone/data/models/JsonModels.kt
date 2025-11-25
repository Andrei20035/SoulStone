package com.example.soulstone.data.models

import com.google.gson.annotations.SerializedName

data class ZodiacJsonItem(
    @SerializedName("key") val key: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("image_name") val imageName: String,
    @SerializedName("translations") val translations: Map<String, ZodiacJsonTranslation>
)

data class ZodiacJsonTranslation(
    @SerializedName("name") val name: String,
    @SerializedName("element") val element: String,
    @SerializedName("planet") val planet: String,
    @SerializedName("desc") val description: String
)

data class BenefitJsonItem(
    @SerializedName("key") val key: String,
    @SerializedName("image_name") val imageName: String,
    @SerializedName("translations") val translations: Map<String, BenefitJsonTranslation>
)

data class BenefitJsonTranslation(
    @SerializedName("name") val name: String
)

data class ChineseZodiacJsonItem(
    @SerializedName("key") val key: String,
    @SerializedName("image_base") val imageBase: String,
    @SerializedName("years") val years: String,
    @SerializedName("translations") val translations: Map<String, ChineseZodiacTranslationData>
)

data class ChineseZodiacTranslationData(
    @SerializedName("name") val name: String,
    @SerializedName("desc") val description: String,
    @SerializedName("traits") val traits: String,
    @SerializedName("best_match") val bestMatch: String,
    @SerializedName("worst_match") val worstMatch: String,
    @SerializedName("compatibility_desc") val compatibilityDesc: String,
    @SerializedName("gemstone_desc") val gemstoneDesc: String
)

data class ChineseIcons(val icon: Int, val border: Int, val color: Int)

data class StoneJsonItem(
    @SerializedName("image_name") val imageName: String,
    @SerializedName("translations") val translations: Map<String, StoneJsonTranslationData>
)

data class StoneJsonTranslationData(
    @SerializedName("name") val name: String,
    @SerializedName("desc") val description: String
)

data class ZodiacStoneAssociationJsonItem(
    @SerializedName("zodiac_key") val zodiacKey: String,
    @SerializedName("stones") val stoneKeys: List<String>
)

data class ChakraJsonItem(
    val key: String,
    @SerializedName("image_base") val imageBase: String,
    val translations: Map<String, ChakraTranslationJsonData>
)

data class ChakraTranslationJsonData(
    val name: String,
    @SerializedName("desc") val description: String,
    val location: String,
    @SerializedName("ruling_planet") val rulingPlanet: String,
    val element: String,
    @SerializedName("stone_colors") val stoneColors: String,
    @SerializedName("healing_qualities") val healingQualities: String,
    val stones: String,
    @SerializedName("body_placement") val bodyPlacement: String,
    @SerializedName("house_placement") val housePlacement: String,
    val herbs: String,
    @SerializedName("essential_oils") val essentialOils: String
)

data class ChakraAssociationJsonItem(
    @SerializedName("chakra_key") val chakraKey: String,
    @SerializedName("stones") val stoneKeys: List<String>
)

data class BenefitAssociationJsonItem(
    @SerializedName("benefit_key") val benefitKey: String,
    @SerializedName("stones") val stoneKeys: List<String>
)

