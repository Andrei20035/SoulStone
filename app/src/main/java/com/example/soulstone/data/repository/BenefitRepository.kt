package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedBenefit
import kotlinx.coroutines.flow.Flow


interface BenefitRepository {
    fun getAllTranslatedBenefits(language: LanguageCode): Flow<List<TranslatedBenefit>>
    suspend fun getTranslatedBenefit(keyName: String, language: LanguageCode): TranslatedBenefit?
    fun getTranslatedBenefitFlow(keyName: String, language: LanguageCode): Flow<TranslatedBenefit?>
    suspend fun insertBenefit(benefit: Benefit): Long? // ID of the new row
    suspend fun deleteBenefit(benefit: Benefit): Int
    suspend fun findBenefitByName(keyName: String): Benefit?
    suspend fun insertTranslation(translation: BenefitTranslation): Long?
    suspend fun deleteTranslation(translation: BenefitTranslation): Int
}