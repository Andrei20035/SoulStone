package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.data.pojos.ChineseSignListItem
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChineseZodiacSign
import com.example.soulstone.data.pojos.ZodiacSignListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChineseZodiacSignDao {

    @Query("""
    SELECT 
        z.id as id, 
        z.keyName AS keyName,
        t.name as signName, 
        z.iconName as imageName
    FROM chinese_zodiac_signs z
    INNER JOIN chinese_sign_translations t ON z.id = t.chineseSignId
    WHERE t.languageCode = :language
""")
    fun getAllChineseZodiacSignListItems(language: LanguageCode): Flow<List<ZodiacSignListItem>>

    @Query(
        """
    SELECT 
        c.recentYears,
        c.iconName,
        c.iconBorderName,
        c.iconColorName,
        t.name,
        t.description,
        t.traits,
        t.bestMatch,
        t.worstMatch,
        t.compatibilityDescription,
        t.gemstoneDescription
    FROM chinese_zodiac_signs AS c
    JOIN chinese_sign_translations AS t ON c.id = t.chineseSignId
    WHERE t.languageCode = :language
    ORDER BY c.id ASC
"""
    )
    fun getAllTranslatedChineseSigns(language: LanguageCode): Flow<List<TranslatedChineseZodiacSign>>

    @Query(
        """
    SELECT 
        c.recentYears,
        c.iconName,
        c.iconBorderName,
        c.iconColorName,
        t.name,
        t.description,
        t.traits,
        t.bestMatch,
        t.worstMatch,
        t.compatibilityDescription,
        t.gemstoneDescription
    FROM chinese_zodiac_signs AS c
    JOIN chinese_sign_translations AS t ON c.id = t.chineseSignId
    WHERE c.keyName = :keyName AND t.languageCode = :language
"""
    )
    suspend fun getTranslatedChineseSign(keyName: String, language: LanguageCode): TranslatedChineseZodiacSign?

    @Query(
        """
    SELECT 
        c.recentYears,
        c.iconName,
        c.iconBorderName,
        c.iconColorName,
        t.name,
        t.description,
        t.traits,
        t.bestMatch,
        t.worstMatch,
        t.compatibilityDescription,
        t.gemstoneDescription
    FROM chinese_zodiac_signs AS c
    JOIN chinese_sign_translations AS t ON c.id = t.chineseSignId
    WHERE c.keyName = :keyName AND t.languageCode = :language
"""
    )
    fun getTranslatedChineseSignFlow(keyName: String, language: LanguageCode): Flow<TranslatedChineseZodiacSign?>


    // --- Admin/Helper Queries for ChineseZodiacSign table ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChineseSign(sign: ChineseZodiacSign): Long

    @Update
    suspend fun updateChineseSign(sign: ChineseZodiacSign): Int

    @Delete
    suspend fun deleteChineseSign(sign: ChineseZodiacSign): Int

    @Query("SELECT * FROM chinese_zodiac_signs WHERE keyName = :keyName LIMIT 1")
    suspend fun getChineseSignByKey(keyName: String): ChineseZodiacSign?

    @Query("SELECT id FROM chinese_zodiac_signs WHERE keyName = :keyName LIMIT 1")
    suspend fun getChineseSignIdByKey(keyName: String): Int?




    // --- Admin/Helper Queries for ChineseZodiacSignTranslation table ---

    @Insert
    suspend fun insertChineseTranslations(translation: List<ChineseZodiacSignTranslation>)

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
                "Failed to insert chinese sign '${chineseSign.keyName}', it might already exist."
            )
        }
        val translationsWithId = translations.map {
            it.copy(chineseSignId = newChineseSignId.toInt())
        }
        insertChineseTranslations(translationsWithId)
    }
}
