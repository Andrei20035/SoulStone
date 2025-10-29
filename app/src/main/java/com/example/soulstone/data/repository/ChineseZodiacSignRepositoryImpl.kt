package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.ChineseZodiacSignDao
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChineseZodiacSign
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChineseZodiacSignRepositoryImpl @Inject constructor(
    private val dao: ChineseZodiacSignDao
): ChineseZodiacSignRepository {
    override fun getAllTranslatedChineseSigns(language: LanguageCode): Flow<List<TranslatedChineseZodiacSign>> {
        return dao.getAllTranslatedChineseSigns(language)
    }

    override fun getTranslatedChineseSignFlow(
        keyName: String,
        language: LanguageCode
    ): Flow<TranslatedChineseZodiacSign?> {
        return dao.getTranslatedChineseSignFlow(keyName, language)
    }

    override suspend fun getTranslatedChineseSign(
        keyName: String,
        language: LanguageCode
    ): TranslatedChineseZodiacSign? {
        return dao.getTranslatedChineseSign(keyName, language)
    }

    override suspend fun insertChineseSignWithTranslations(
        chineseSign: ChineseZodiacSign,
        translations: List<ChineseZodiacSignTranslation>
    ) {
        return dao.insertChineseSignWithTranslations(chineseSign, translations)
    }

    override suspend fun updateTranslation(translation: ChineseZodiacSignTranslation): Int {
        return dao.updateTranslation(translation)
    }

    override suspend fun updateChineseSign(sign: ChineseZodiacSign): Int {
        return dao.updateChineseSign(sign)
    }

    override suspend fun deleteChineseSign(sign: ChineseZodiacSign): Int {
        return dao.deleteChineseSign(sign)
    }

    override suspend fun getChineseSignByName(keyName: String): ChineseZodiacSign? {
        return dao.getChineseSignByName(keyName)
    }

}