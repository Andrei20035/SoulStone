package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import kotlinx.coroutines.flow.Flow


interface ZodiacSignRepository {

    fun getAllTranslatedZodiacSigns(language: LanguageCode): Flow<List<TranslatedZodiacSign>>
    fun getTranslatedZodiacSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedZodiacSign?>
    suspend fun getTranslatedZodiacSign(keyName: String, language: LanguageCode): TranslatedZodiacSign?
    suspend fun insertZodiacSignWithTranslations(
        zodiacSign: ZodiacSign,
        translations: List<ZodiacSignTranslation>
    )
    suspend fun updateTranslation(translation: ZodiacSignTranslation): Int
    suspend fun updateZodiacSign(sign: ZodiacSign): Int
    suspend fun deleteZodiacSign(sign: ZodiacSign): Int
}