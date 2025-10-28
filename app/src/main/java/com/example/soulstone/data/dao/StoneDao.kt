package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedStone
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.wrappers.StoneWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface StoneDao {

    // --- Main App List Queries (Simple JOINs) ---

    /**
     * Fetches a list of all stones, translated into the specified language.
     * Used for the main "All Stones" list.
     */
    @Query("""
        SELECT
            s.id AS id, 
            s.name AS keyName, 
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

    /**
     * Fetches a single stone with its translation.
     */
    @Query("""
        SELECT
            s.id AS id, 
            s.name AS keyName, 
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


    // --- Filtered List Queries (3-Table JOINs) ---

    /**
     * Fetches all stones associated with a specific Benefit (by its keyName),
     * translated into the specified language.
     */
    @Query("""
        SELECT
            s.id AS id, 
            s.name AS keyName, 
            s.imageUri AS imageUri,
            st.name AS translatedName,
            st.description AS description,
            st.languageCode AS languageCode
        FROM stones AS s
        JOIN stone_translations AS st ON s.id = st.stoneId
        JOIN stone_benefit_cross_ref AS sb ON s.id = sb.stoneId
        JOIN benefits AS b ON sb.benefitId = b.id
        WHERE b.name = :benefitKeyName AND st.languageCode = :language
        ORDER BY st.name ASC
    """)
    fun getStonesForBenefit(benefitKeyName: String, language: LanguageCode): Flow<List<TranslatedStone>>

    /**
     * Fetches all stones associated with a specific Chakra (by its sanskritName),
     * translated into the specified language.
     */
    @Query("""
        SELECT
            s.id AS id, 
            s.name AS keyName, 
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

    // ... (You can add similar queries for ZodiacSign and ChineseZodiacSign) ...


    // --- Detail Screen Query (@Transaction) ---

    /**
     * Fetches the full details for a single stone, including all its
     * translations and related *key* objects (Benefit, Chakra, etc.).
     * The Repository will be responsible for translating the related lists.
     */
    @Transaction
    @Query("SELECT * FROM stones WHERE name = :keyName LIMIT 1")
    suspend fun getStoneDetails(keyName: String): StoneWithDetails?


    // --- Admin/Helper Queries ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStone(stone: Stone)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: StoneTranslation)

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