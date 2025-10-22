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
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.wrappers.StoneWithDetails

@Dao
interface StoneDao {

    // -- Stone functions --
    @Transaction
    @Query("SELECT * FROM stones")
    suspend fun getAllStones(): List<StoneWithDetails>

    @Transaction
    @Query("SELECT * FROM stones WHERE id = :stoneId")
    suspend fun getStoneById(stoneId: Int): StoneWithDetails?

    @Transaction
    @Query("""
        SELECT DISTINCT stones.* FROM stones 
        INNER JOIN stone_translations ON stones.id = stone_translations.stoneId
        WHERE stone_translations.languageCode = :lang
        AND (stone_translations.name LIKE '%' || :query || '%' 
             OR stone_translations.description LIKE '%' || :query || '%')
    """)
    suspend fun searchStones(lang: String, query: String): List<StoneWithDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStone(stone: Stone): Long

    @Transaction
    suspend fun insertFullStone(
        stone: Stone,
        translations: List<StoneTranslation>,
        benefitIds: List<Int>,
        chakraIds: List<Int>,
        zodiacSignIds: List<Int>,
        chineseZodiacSignIds: List<Int>
    ) {
        // 1. Insert the parent Stone and get its new ID
        val newStoneId = insertStone(stone).toInt()

        // 2. Assign the new ID to all translations and insert them
        val translationsWithId = translations.map { it.copy(stoneId = newStoneId) }
        // Note: It's more efficient to have a DAO function that inserts a List
        // @Insert suspend fun insertTranslations(translations: List<StoneTranslation>)
        insertTranslations(translationsWithId)

        // 3. Create and insert the Benefit cross-references
        val benefitCrossRefs = benefitIds.map { id ->
            StoneBenefitCrossRef(stoneId = newStoneId, benefitId = id)
        }
        insertBenefitCrossRefs(benefitCrossRefs)

        // 4. Create and insert the Chakra cross-references
        val chakraCrossRefs = chakraIds.map { id ->
            StoneChakraCrossRef(stoneId = newStoneId, chakraId = id)
        }
        insertChakraCrossRefs(chakraCrossRefs)

        // 5. Create and insert the Zodiac Sign cross-references
        val zodiacCrossRefs = zodiacSignIds.map { id ->
            StoneZodiacCrossRef(stoneId = newStoneId, zodiacSignId = id)
        }
        insertZodiacCrossRefs(zodiacCrossRefs)

        // 6. Create and insert the Chinese Zodiac Sign cross-references
        val chineseZodiacCrossRefs = chineseZodiacSignIds.map { id ->
            StoneChineseZodiacCrossRef(stoneId = newStoneId, chineseZodiacSignId = id)
        }
        insertChineseZodiacCrossRefs(chineseZodiacCrossRefs)
    }

    @Delete
    suspend fun deleteStone(stone: Stone)

    // -- Translation functions --
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: StoneTranslation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslations(translations: List<StoneTranslation>)

    @Update
    suspend fun updateTranslation(translation: StoneTranslation)

    // --- Functions for benefit relationships ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenefitCrossRefs(joins: List<StoneBenefitCrossRef>)

    @Delete
    suspend fun deleteBenefitCrossRefs(joins: List<StoneBenefitCrossRef>)

    // --- Functions for chakra relationships ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChakraCrossRefs(joins: List<StoneChakraCrossRef>)

    @Delete
    suspend fun deleteChakraCrossRefs(joins: List<StoneChakraCrossRef>)

    // --- Functions for Zodiac sign relationships ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZodiacCrossRefs(joins: List<StoneZodiacCrossRef>)

    @Delete
    suspend fun deleteZodiacCrossRefs(joins: List<StoneZodiacCrossRef>)

    // --- Functions for chinese zodiac sign relationships ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChineseZodiacCrossRefs(joins: List<StoneChineseZodiacCrossRef>)

    @Delete
    suspend fun deleteChineseZodiacCrossRefs(joins: List<StoneChineseZodiacCrossRef>)

}
