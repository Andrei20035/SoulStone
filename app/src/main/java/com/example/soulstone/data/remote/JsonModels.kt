package com.example.soulstone.data.remote

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    val data: TranslationData
)

data class TranslationData(
    val translations: List<TranslationResult>
)

data class TranslationResult(
    @SerializedName("translatedText") val translatedText: String,
    val detectedSourceLanguage: String? = null
)

// The body we send to the API (POST request is safer for long descriptions)
data class TranslationRequest(
    val q: List<String>,      // List of strings to translate (Name, Description)
    val target: String,       // Target language code (e.g., "es")
    val format: String = "text" // "text" or "html"
)