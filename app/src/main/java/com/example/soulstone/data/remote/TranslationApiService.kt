package com.example.soulstone.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationApiService {
    @POST("language/translate/v2")
    suspend fun translate(
        @Query("key") apiKey: String, // Pass API Key here
        @Body request: TranslationRequest
    ): TranslationResponse
}