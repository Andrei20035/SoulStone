package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedBenefit
import kotlinx.coroutines.flow.Flow


interface BenefitRepository {
    fun getAllTranslatedBenefits(language: LanguageCode): Flow<List<TranslatedBenefit>>
    suspend fun getTranslatedBenefit(keyName: String, language: LanguageCode): TranslatedBenefit?

    suspend fun getTranslatedBenefitById(benefitId: Int, language: LanguageCode): TranslatedBenefit?
    fun getTranslatedBenefitFlowById(benefitId: Int, language: LanguageCode): Flow<TranslatedBenefit?>
    fun getTranslatedBenefitFlow(keyName: String, language: LanguageCode): Flow<TranslatedBenefit?>
    suspend fun insertBenefit(benefit: Benefit): Long? // ID of the new row
    suspend fun deleteBenefit(benefit: Benefit): Int
    suspend fun findBenefitByName(keyName: String): Benefit?
    suspend fun insertTranslations(translations: List<BenefitTranslation>)

    suspend fun updateTranslation(translation: BenefitTranslation): Int

    suspend fun insertBenefitWithTranslations(benefit: Benefit, translations: List<BenefitTranslation>)
}