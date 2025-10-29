package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedBenefit
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
    fun getTranslatedBenefitFlow(keyName: String, language: LanguageCode): Flow<TranslatedBenefit?>


    // --- Admin/Helper Queries for Benefit table ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBenefit(benefit: Benefit): Long?

    @Delete
    suspend fun deleteBenefit(benefit: Benefit): Int

    @Query("SELECT * FROM benefits WHERE name = :keyName LIMIT 1")
    suspend fun findBenefitByName(keyName: String): Benefit?


    // --- Admin/Helper Queries for BenefitTranslation table ---

    @Insert
    suspend fun insertTranslations(translations: List<BenefitTranslation>)

    @Update
    suspend fun updateTranslation(translation: BenefitTranslation): Int

    @Transaction
    suspend fun insertBenefitWithTranslations(
        benefit: Benefit,
        translations: List<BenefitTranslation>
    ) {

        val newBenefitId = insertBenefit(benefit)

        if(newBenefitId == null) {
            throw IllegalStateException(
                "Failed to insert benefit '${benefit.name}', it might already exist."
            )
        }

        val translationsWithId = translations.map {
            it.copy(benefitId = newBenefitId.toInt())
        }

        insertTranslations(translationsWithId)
    }
}
