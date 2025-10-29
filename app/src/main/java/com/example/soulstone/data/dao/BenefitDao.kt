package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedBenefit
import kotlinx.coroutines.flow.Flow

@Dao
interface BenefitDao {

    // --- Main App Queries (using JOINs) ---

    @Query("""
        SELECT 
            b.id AS id, 
            b.name AS keyName, 
            t.name AS translatedName,
            t.languageCode AS languageCode
        FROM benefits AS b
        JOIN benefit_translations AS t ON b.id = t.benefitId
        WHERE t.languageCode = :language
        ORDER BY t.name ASC
    """)
    fun getAllTranslatedBenefits(language: LanguageCode): Flow<List<TranslatedBenefit>>

    @Query("""
        SELECT 
            b.id AS id, 
            b.name AS keyName, 
            t.name AS translatedName,
            t.languageCode AS languageCode
        FROM benefits AS b
        JOIN benefit_translations AS t ON b.id = t.benefitId
        WHERE b.name = :keyName AND t.languageCode = :language
    """)
    suspend fun getTranslatedBenefit(keyName: String, language: LanguageCode): TranslatedBenefit?
    fun getTranslatedBenefitFlow(keyName: String, language: LanguageCode): Flow<TranslatedBenefit?>


    // --- Admin/Helper Queries for Benefit table ---

    suspend fun insertBenefit(benefit: Benefit): Long?

    @Update
    suspend fun updateBenefit(benefit: Benefit): Int

    @Delete
    suspend fun deleteBenefit(benefit: Benefit): Int

    @Query("SELECT * FROM benefits WHERE name = :keyName LIMIT 1")
    suspend fun findBenefitByName(keyName: String): Benefit?


    // --- Admin/Helper Queries for BenefitTranslation table ---

    suspend fun insertTranslation(translation: BenefitTranslation): Long?

    @Update
    suspend fun updateTranslation(translation: BenefitTranslation): Int

    @Delete
    suspend fun deleteTranslation(translation: BenefitTranslation): Int

    @Query("SELECT * FROM benefit_translations WHERE benefitId = :benefitId AND languageCode = :languageCode LIMIT 1")
    suspend fun findTranslation(benefitId: Int, languageCode: LanguageCode): BenefitTranslation?
}
