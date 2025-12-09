package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.dao.ZodiacSignDao
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import com.example.soulstone.data.pojos.ZodiacSignListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ZodiacSignRepositoryImpl @Inject constructor(
    private val zodiacSignDao: ZodiacSignDao,
    private val stoneDao: StoneDao,
) : ZodiacSignRepository {
    override suspend fun getSignByName(signName: String): ZodiacSign? {
        return zodiacSignDao.getZodiacSignByName(signName)
    }

    override fun getZodiacSignListItems(language: LanguageCode): Flow<List<ZodiacSignListItem>> {
        return zodiacSignDao.getAllZodiacSignListItems(language)
    }

    override fun getAllTranslatedZodiacSigns(language: LanguageCode): Flow<List<TranslatedZodiacSign>> {
        return zodiacSignDao.getAllTranslatedZodiacSigns(language)
    }

    override fun getTranslatedZodiacSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedZodiacSign?> {
        return zodiacSignDao.getTranslatedZodiacSignFlow(keyName, language)
    }

    override fun getStonesForSignFlow(
        signName: String,
        languageCode: LanguageCode,
        limit: Int
    ): Flow<List<StoneListItem>> {
        return stoneDao.getStonesForSignFlow(signName, languageCode, limit)
    }

    override suspend fun getTranslatedZodiacSign(keyName: String, language: LanguageCode): TranslatedZodiacSign? {
        return zodiacSignDao.getTranslatedZodiacSign(keyName, language)
    }

    override suspend fun insertZodiacSignWithTranslations(
        zodiacSign: ZodiacSign,
        translations: List<ZodiacSignTranslation>
    ) {
        zodiacSignDao.insertZodiacSignWithTranslations(zodiacSign, translations)
    }

    override suspend fun updateTranslation(translation: ZodiacSignTranslation): Int {
        return zodiacSignDao.updateTranslation(translation)
    }

    override suspend fun updateZodiacSign(sign: ZodiacSign): Int {
        return zodiacSignDao.updateZodiacSign(sign)
    }

    override suspend fun deleteZodiacSign(sign: ZodiacSign): Int {
        return zodiacSignDao.deleteZodiacSign(sign)
    }

    override suspend fun dbInitialization() {
        zodiacSignDao.getSignCount()
    }
}
