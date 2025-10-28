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

    /**
     * Fetches a list of all benefits, translated into the specified language.
     * This is the main query you'll use to display a list of benefits in the app.
     */
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

    /**
     * Fetches a single benefit, translated into the specified language, by its stable English name.
     */
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


    // --- Admin/Helper Queries for Benefit table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenefit(benefit: Benefit)

    @Update
    suspend fun updateBenefit(benefit: Benefit)

    @Delete
    suspend fun deleteBenefit(benefit: Benefit)

    @Query("SELECT * FROM benefits WHERE name = :keyName LIMIT 1")
    suspend fun getBenefitByName(keyName: String): Benefit?


    // --- Admin/Helper Queries for BenefitTranslation table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: BenefitTranslation)

    @Update
    suspend fun updateTranslation(translation: BenefitTranslation)

    @Delete
    suspend fun deleteTranslation(translation: BenefitTranslation)
}
