package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.BenefitDao
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedBenefit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BenefitRepositoryImpl @Inject constructor(
    private val dao: BenefitDao
) : BenefitRepository {
    override fun getAllTranslatedBenefits(language: LanguageCode): Flow<List<TranslatedBenefit>> {
        return dao.getAllTranslatedBenefits(language)
    }

    override suspend fun getTranslatedBenefit(
        keyName: String,
        language: LanguageCode
    ): TranslatedBenefit? {
        return dao.getTranslatedBenefit(keyName, language)
    }

    override fun getTranslatedBenefitFlow(
        keyName: String,
        language: LanguageCode
    ): Flow<TranslatedBenefit?> {
        return dao.getTranslatedBenefitFlow(keyName, language)
    }

    override suspend fun insertBenefit(benefit: Benefit): Long? {
        val existingBenefit = dao.findBenefitByName(benefit.name)

        if(existingBenefit == null) {
            return dao.insertBenefit(benefit)
        } else {
            return existingBenefit.id.toLong()
        }
    }

    override suspend fun deleteBenefit(benefit: Benefit): Int {
        return dao.deleteBenefit(benefit)
    }

    override suspend fun findBenefitByName(keyName: String): Benefit? {
        return dao.findBenefitByName(keyName)
    }

    override suspend fun insertTranslation(translation: BenefitTranslation): Long? {
        val existingTranslation = dao.findTranslation(translation.benefitId, translation.languageCode)

        if (existingTranslation == null) {
            return dao.insertTranslation(translation)
        } else {
            val updatedTranslation = translation.copy(id = existingTranslation.id)
            return dao.updateTranslation(updatedTranslation).toLong()
        }
    }

    override suspend fun deleteTranslation(translation: BenefitTranslation): Int {
        return dao.deleteTranslation(translation)
    }
}