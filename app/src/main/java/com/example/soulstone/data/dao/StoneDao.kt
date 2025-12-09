package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.pojos.StoneWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface StoneDao {

    @Query("""
    SELECT 
        s.id as id, 
        st.name as name, 
        s.imageUri 
    FROM stones s
    INNER JOIN stone_translations st ON s.id = st.stoneId
    INNER JOIN stone_zodiac_cross_ref ref ON s.id = ref.stoneId
    INNER JOIN zodiac_signs z ON ref.zodiacSignId = z.id
    WHERE z.name = :signName 
      AND st.languageCode = :languageCode
    LIMIT :limit
""")
    fun getStonesForSignFlow(
        signName: String,
        languageCode: LanguageCode,
        limit: Int
    ): Flow<List<StoneListItem>>

    @Query(
        """
    SELECT 
        s.id as id, 
        st.name as name, 
        s.imageUri 
    FROM stones s
    INNER JOIN stone_translations st ON s.id = st.stoneId
    INNER JOIN stone_chinese_zodiac_cross_ref ref ON s.id = ref.stoneId
    INNER JOIN chinese_zodiac_signs z ON ref.chineseZodiacSignId = z.id
    WHERE z.keyName = :keyName 
      AND st.languageCode = :languageCode
    LIMIT :limit
"""
    )
    fun getStonesForChineseSignFlow(
        keyName: String,
        languageCode: LanguageCode,
        limit: Int
    ): Flow<List<StoneListItem>>

    @Query("""
        SELECT
            s.id AS id, 
            s.imageUri AS imageUri,
            t.name AS translatedName,
            t.description AS description,
            t.languageCode AS languageCode
        FROM stones AS s
        JOIN stone_translations AS t ON s.id = t.stoneId
        WHERE t.languageCode = :language
        ORDER BY t.name ASC
    """)
    fun getAllTranslatedStones(language: LanguageCode): Flow<List<TranslatedStone>>


    @Query("""
        SELECT
            s.id AS id, 
            s.imageUri AS imageUri,
            t.name AS translatedName,
            t.description AS description,
            t.languageCode AS languageCode
        FROM stones AS s
        JOIN stone_translations AS t ON s.id = t.stoneId
        WHERE s.name = :keyName AND t.languageCode = :language
        LIMIT 1
    """)
    suspend fun getTranslatedStone(keyName: String, language: LanguageCode): TranslatedStone?

    @Query("""
    SELECT
        s.id AS id,
        s.imageUri AS imageUri,
        t.name AS translatedName,
        t.description AS description,
        t.languageCode AS languageCode
    FROM stones AS s
    JOIN stone_translations AS t ON s.id = t.stoneId
    WHERE s.id = :stoneId AND t.languageCode = :language
    LIMIT 1
""")
    fun getTranslatedStoneFlow(stoneId: Int, language: LanguageCode): Flow<TranslatedStone?>


    // --- Filtered List Queries (3-Table JOINs) ---

    @Query("""
    SELECT
        s.id AS id, 
        s.imageUri AS imageUri,
        st.name AS translatedName,
        st.description AS description,
        st.languageCode AS languageCode
    FROM stones AS s
    JOIN stone_translations AS st ON s.id = st.stoneId
    JOIN stone_benefit_cross_ref AS sb ON s.id = sb.stoneId
    JOIN benefits AS b ON sb.benefitId = b.id
    WHERE b.id = :benefitId AND st.languageCode = :language
    ORDER BY st.name ASC
""")
    fun getStonesForBenefit(benefitId: Int, language: LanguageCode): Flow<List<TranslatedStone>>

    @Query("""
        SELECT
            s.id AS id, 
            s.imageUri AS imageUri,
            st.name AS translatedName,
            st.description AS description,
            st.languageCode AS languageCode
        FROM stones AS s
        JOIN stone_translations AS st ON s.id = st.stoneId
        JOIN stone_chakra_cross_ref AS sc ON s.id = sc.stoneId
        JOIN chakras AS c ON sc.chakraId = c.id
        WHERE c.sanskritName = :chakraSanskritName AND st.languageCode = :language
        ORDER BY st.name ASC
    """)
    fun getStonesForChakra(chakraSanskritName: String, language: LanguageCode): Flow<List<TranslatedStone>>



    // --- Detail Screen Query (@Transaction) ---

    @Transaction
    @Query("SELECT * FROM stones WHERE name = :keyName LIMIT 1")
    suspend fun getStoneDetails(keyName: String): StoneWithDetails?


    // --- Admin/Helper Queries ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStone(stone: Stone): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: StoneTranslation)

    @Transaction
    suspend fun insertStoneWithTranslations(stone: Stone, translations: List<StoneTranslation>) {
        val stoneId = insertStone(stone)

        translations.forEach { translation ->
            val linkedTranslation = translation.copy(stoneId = stoneId.toInt())
            insertTranslation(linkedTranslation)
        }
    }

    @Query("SELECT id FROM stones WHERE name = :keyName LIMIT 1")
    suspend fun getStoneIdByKey(keyName: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBenefitCrossRef(crossRef: StoneBenefitCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChakraCrossRef(crossRef: StoneChakraCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertZodiacCrossRef(crossRef: StoneZodiacCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChineseZodiacCrossRef(crossRef: StoneChineseZodiacCrossRef)

    @Delete
    suspend fun deleteBenefitCrossRef(crossRef: StoneBenefitCrossRef)
    // ... (add other delete/update methods as needed) ...
}