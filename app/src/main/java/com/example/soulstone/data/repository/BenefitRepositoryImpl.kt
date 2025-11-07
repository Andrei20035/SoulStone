package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.BenefitDao
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedBenefit
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

    override suspend fun getTranslatedBenefitById(
        benefitId: Int,
        language: LanguageCode
    ): TranslatedBenefit? {
        return dao.getTranslatedBenefitById(benefitId, language)
    }

    override fun getTranslatedBenefitFlowById(
        benefitId: Int,
        language: LanguageCode
    ): Flow<TranslatedBenefit?> {
        return dao.getTranslatedBenefitFlowById(benefitId, language)
    }

    override fun getTranslatedBenefitFlow(
        keyName: String,
        language: LanguageCode
    ): Flow<TranslatedBenefit?> {
        return dao.getTranslatedBenefitFlow(keyName, language)
    }

    override suspend fun insertBenefit(benefit: Benefit): Long? {
        val newId = insertBenefit(benefit)

        if (newId == -1L) {
            return findBenefitByName(benefit.name)!!.id.toLong()
        } else {
            return newId
        }
    }

    override suspend fun deleteBenefit(benefit: Benefit): Int {
        return dao.deleteBenefit(benefit)
    }

    override suspend fun findBenefitByName(keyName: String): Benefit? {
        return dao.findBenefitByName(keyName)
    }

    override suspend fun insertTranslations(translations: List<BenefitTranslation>) {
        return dao.insertTranslations(translations)
    }

    override suspend fun updateTranslation(translation: BenefitTranslation): Int {
        return dao.updateTranslation(translation)
    }

    override suspend fun insertBenefitWithTranslations(
        benefit: Benefit,
        translations: List<BenefitTranslation>
    ) {
        return dao.insertBenefitWithTranslations(benefit, translations)
    }
}