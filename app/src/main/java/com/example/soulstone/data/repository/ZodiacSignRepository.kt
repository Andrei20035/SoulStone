package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import com.example.soulstone.data.pojos.ZodiacSignListItem
import kotlinx.coroutines.flow.Flow


interface ZodiacSignRepository {
    suspend fun getSignByName(signName: String): ZodiacSign?

    fun getZodiacSignListItems(language: LanguageCode): Flow<List<ZodiacSignListItem>>
    fun getAllTranslatedZodiacSigns(language: LanguageCode): Flow<List<TranslatedZodiacSign>>
    fun getTranslatedZodiacSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedZodiacSign?>
    fun getStonesForSignFlow(signName: String, languageCode: LanguageCode, limit: Int): Flow<List<StoneListItem>>
    suspend fun getTranslatedZodiacSign(keyName: String, language: LanguageCode): TranslatedZodiacSign?
    suspend fun insertZodiacSignWithTranslations(
        zodiacSign: ZodiacSign,
        translations: List<ZodiacSignTranslation>
    )
    suspend fun updateTranslation(translation: ZodiacSignTranslation): Int
    suspend fun updateZodiacSign(sign: ZodiacSign): Int
    suspend fun deleteZodiacSign(sign: ZodiacSign): Int
    suspend fun dbInitialization()
}