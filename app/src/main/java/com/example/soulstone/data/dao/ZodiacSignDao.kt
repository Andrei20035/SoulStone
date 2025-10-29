package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedZodiacSign
import kotlinx.coroutines.flow.Flow

@Dao
interface ZodiacSignDao {

    @Query(
        """
    SELECT 
        z.id AS id, 
        z.name AS keyName, 
        z.startDate AS startDate, 
        z.endDate AS endDate,
        t.name AS translatedName,
        t.description AS description,
        t.element AS element,
        t.rulingPlanet AS rulingPlanet,
        t.languageCode AS languageCode
    FROM zodiac_signs AS z
    JOIN zodiac_translations AS t ON z.id = t.zodiacSignId
    WHERE t.languageCode = :language
    ORDER BY z.id ASC
"""
    )
    fun getAllTranslatedZodiacSigns(language: LanguageCode): Flow<List<TranslatedZodiacSign>>

    @Query(
        """
    SELECT 
        z.id AS id, 
        z.name AS keyName, 
        z.startDate AS startDate, 
        z.endDate AS endDate,
        t.name AS translatedName,
        t.description AS description,
        t.element AS element,
        t.rulingPlanet AS rulingPlanet,
        t.languageCode AS languageCode
    FROM zodiac_signs AS z
    JOIN zodiac_translations AS t ON z.id = t.zodiacSignId
    WHERE z.name = :keyName AND t.languageCode = :language
    LIMIT 1
"""
    )
    fun getTranslatedZodiacSignFlow(
        keyName: String,
        language: LanguageCode
    ): Flow<TranslatedZodiacSign?>

    @Query(
        """
    SELECT 
        z.id AS id, 
        z.name AS keyName, 
        z.startDate AS startDate, 
        z.endDate AS endDate,
        t.name AS translatedName,
        t.description AS description,
        t.element AS element,
        t.rulingPlanet AS rulingPlanet,
        t.languageCode AS languageCode
    FROM zodiac_signs AS z
    JOIN zodiac_translations AS t ON z.id = t.zodiacSignId
    WHERE z.name = :keyName AND t.languageCode = :language
    LIMIT 1
"""
    )
    suspend fun getTranslatedZodiacSign(
        keyName: String,
        language: LanguageCode
    ): TranslatedZodiacSign?

    @Update
    suspend fun updateTranslation(translation: ZodiacSignTranslation): Int

    @Delete
    suspend fun deleteZodiacSign(sign: ZodiacSign): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertZodiacSign(sign: ZodiacSign): Long?

    @Update
    suspend fun updateZodiacSign(sign: ZodiacSign): Int

    @Query("SELECT * FROM zodiac_signs WHERE id = :signId")
    suspend fun getZodiacSignById(signId: Int): ZodiacSign?

    @Query("SELECT * FROM zodiac_signs ORDER BY name ASC")
    fun getAllZodiacSigns(): Flow<List<ZodiacSign>>

    @Query("SELECT * FROM zodiac_signs WHERE name = :signName LIMIT 1")
    suspend fun getZodiacSignByName(signName: String): ZodiacSign?

    @Insert
    suspend fun insertTranslations(translations: List<ZodiacSignTranslation>)

    @Transaction
    suspend fun insertZodiacSignWithTranslations(
        zodiacSign: ZodiacSign,
        translations: List<ZodiacSignTranslation>
    ) {
        val newZodiacSignId = insertZodiacSign(zodiacSign)
        if (newZodiacSignId == null) {
            throw IllegalStateException(
                "Failed to insert zodiacSign '${zodiacSign.name}', it might already exist."
            )
        }
        val translationsWithId = translations.map {
            it.copy(zodiacSignId = newZodiacSignId.toInt())
        }
        insertTranslations(translationsWithId)
    }

}