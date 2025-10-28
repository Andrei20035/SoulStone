package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedChineseZodiacSign
import kotlinx.coroutines.flow.Flow

@Dao
interface ChineseZodiacSignDao {

    // --- Main App Queries (using JOINs) ---

    /**
     * Fetches a list of all Chinese zodiac signs, translated into the specified language.
     * This is the main query you'll use to display a list of signs in the app.
     */
    @Query("""
        SELECT 
            c.id AS id, 
            c.name AS keyName, 
            c.recentYears AS recentYears,
            t.name AS translatedName,
            t.description AS description,
            t.languageCode AS languageCode
        FROM chinese_zodiac_signs AS c
        JOIN chinese_sign_translations AS t ON c.id = t.chineseSignId
        WHERE t.languageCode = :language
        ORDER BY c.id ASC
    """)
    fun getAllTranslatedChineseSigns(language: LanguageCode): Flow<List<TranslatedChineseZodiacSign>>

    /**
     * Fetches a single Chinese zodiac sign, translated into the specified language,
     * by its stable English name.
     */
    @Query("""
        SELECT 
            c.id AS id, 
            c.name AS keyName, 
            c.recentYears AS recentYears,
            t.name AS translatedName,
            t.description AS description,
            t.languageCode AS languageCode
        FROM chinese_zodiac_signs AS c
        JOIN chinese_sign_translations AS t ON c.id = t.chineseSignId
        WHERE c.name = :keyName AND t.languageCode = :language
    """)
    suspend fun getTranslatedChineseSign(keyName: String, language: LanguageCode): TranslatedChineseZodiacSign?


    // --- Admin/Helper Queries for ChineseZodiacSign table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChineseSign(sign: ChineseZodiacSign)

    @Update
    suspend fun updateChineseSign(sign: ChineseZodiacSign)

    @Delete
    suspend fun deleteChineseSign(sign: ChineseZodiacSign)

    @Query("SELECT * FROM chinese_zodiac_signs WHERE name = :keyName LIMIT 1")
    suspend fun getChineseSignByName(keyName: String): ChineseZodiacSign?


    // --- Admin/Helper Queries for ChineseZodiacSignTranslation table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: ChineseZodiacSignTranslation)

    @Update
    suspend fun updateTranslation(translation: ChineseZodiacSignTranslation)

    @Delete
    suspend fun deleteTranslation(translation: ChineseZodiacSignTranslation)
}
