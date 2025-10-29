package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedChineseZodiacSign
import kotlinx.coroutines.flow.Flow

@Dao
interface ChineseZodiacSignDao {

    // --- Main App Queries (using JOINs) ---

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
    fun getTranslatedChineseSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedChineseZodiacSign?>


    // --- Admin/Helper Queries for ChineseZodiacSign table ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChineseSign(sign: ChineseZodiacSign): Long?

    @Update
    suspend fun updateChineseSign(sign: ChineseZodiacSign): Int

    @Delete
    suspend fun deleteChineseSign(sign: ChineseZodiacSign): Int

    @Query("SELECT * FROM chinese_zodiac_signs WHERE name = :keyName LIMIT 1")
    suspend fun getChineseSignByName(keyName: String): ChineseZodiacSign?


    // --- Admin/Helper Queries for ChineseZodiacSignTranslation table ---

    @Insert
    suspend fun insertTranslations(translation: List<ChineseZodiacSignTranslation>)

    @Update
    suspend fun updateTranslation(translation: ChineseZodiacSignTranslation): Int

    @Transaction
    suspend fun insertChineseSignWithTranslations(
        chineseSign: ChineseZodiacSign,
        translations: List<ChineseZodiacSignTranslation>
    ) {
        val newChineseSignId = insertChineseSign(chineseSign)
        if(newChineseSignId == null) {
            throw IllegalStateException(
                "Failed to insert chinese sign '${chineseSign.name}', it might already exist."
            )
        }
        val translationsWithId = translations.map {
            it.copy(chineseSignId = newChineseSignId.toInt())
        }
        insertTranslations(translationsWithId)
    }
}
