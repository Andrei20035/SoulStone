package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChineseZodiacSign
import kotlinx.coroutines.flow.Flow

interface ChineseZodiacSignRepository {

    fun getAllTranslatedChineseSigns(language: LanguageCode): Flow<List<TranslatedChineseZodiacSign>>
    fun getTranslatedChineseSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedChineseZodiacSign?>
    suspend fun getTranslatedChineseSign(keyName: String, language: LanguageCode): TranslatedChineseZodiacSign?
    suspend fun insertChineseSignWithTranslations(chineseSign: ChineseZodiacSign, translations: List<ChineseZodiacSignTranslation>)
    suspend fun updateTranslation(translation: ChineseZodiacSignTranslation): Int
    suspend fun updateChineseSign(sign: ChineseZodiacSign): Int
    suspend fun deleteChineseSign(sign: ChineseZodiacSign): Int
    suspend fun getChineseSignByName(keyName: String): ChineseZodiacSign?
}
