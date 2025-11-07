package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.ZodiacSignDao
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ZodiacSignRepositoryImpl @Inject constructor(
    private val dao: ZodiacSignDao
) : ZodiacSignRepository {
    override suspend fun getSignByName(signName: String): ZodiacSign? {
        return dao.getZodiacSignByName(signName)
    }

    override fun getAllTranslatedZodiacSigns(language: LanguageCode): Flow<List<TranslatedZodiacSign>> {
        return dao.getAllTranslatedZodiacSigns(language)
    }

    override fun getTranslatedZodiacSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedZodiacSign?> {
        return dao.getTranslatedZodiacSignFlow(keyName, language)
    }

    override suspend fun getTranslatedZodiacSign(keyName: String, language: LanguageCode): TranslatedZodiacSign? {
        return dao.getTranslatedZodiacSign(keyName, language)
    }

    override suspend fun insertZodiacSignWithTranslations(
        zodiacSign: ZodiacSign,
        translations: List<ZodiacSignTranslation>
    ) {
        dao.insertZodiacSignWithTranslations(zodiacSign, translations)
    }

    override suspend fun updateTranslation(translation: ZodiacSignTranslation): Int {
        return dao.updateTranslation(translation)
    }

    override suspend fun updateZodiacSign(sign: ZodiacSign): Int {
        return dao.updateZodiacSign(sign)
    }

    override suspend fun deleteZodiacSign(sign: ZodiacSign): Int {
        return dao.deleteZodiacSign(sign)
    }

    override suspend fun dbInitialization() {
        dao.getSignCount()
    }
}
