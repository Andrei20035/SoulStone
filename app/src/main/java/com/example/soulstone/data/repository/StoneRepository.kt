package com.example.soulstone.data.repository

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

/**
 * Repository interface for all data related to Stones.
 * This is the single source of truth for the ViewModels.
 */
interface StoneRepository {

    // --- Main App List Queries ---

    /**
     * Gets a flow of all stones, translated into the given language.
     */
    fun getAllTranslatedStones(language: LanguageCode): Flow<List<TranslatedStone>>

    /**
     * Gets a flow of stones filtered by a specific benefit (key name), translated.
     */
    fun getStonesForBenefit(benefitKeyName: String, language: LanguageCode): Flow<List<TranslatedStone>>

    /**
     * Gets a flow of stones filtered by a specific chakra (sanskrit name), translated.
     */
    fun getStonesForChakra(chakraSanskritName: String, language: LanguageCode): Flow<List<TranslatedStone>>

    // ... (Add fun getStonesForZodiac, etc. here) ...


    // --- Detail Screen Query ---

    /**
     * Gets a single stone with all its related details (all translations, benefits, chakras, etc.).
     * The related lists (benefits, chakras) will be untranslated key objects.
     */
    suspend fun getStoneDetails(keyName: String): StoneWithDetails?

    /**
     * Helper to get just one stone's basic translation. (Optional, but matches your DAO)
     */
    suspend fun getTranslatedStone(keyName: String, language: LanguageCode): TranslatedStone?


    // --- Admin/Write Operations ---

    /**
     * Inserts a new stone.
     */
    suspend fun insertStone(stone: Stone)

    /**
     * Inserts or updates a translation for a stone.
     */
    suspend fun insertTranslation(translation: StoneTranslation)

    /**
     * Creates a link between a stone and a benefit.
     */
    suspend fun insertBenefitCrossRef(crossRef: StoneBenefitCrossRef)

    /**
     * Creates a link between a stone and a chakra.
     */
    suspend fun insertChakraCrossRef(crossRef: StoneChakraCrossRef)

    /**
     * Creates a link between a stone and a zodiac sign.
     */
    suspend fun insertZodiacCrossRef(crossRef: StoneZodiacCrossRef)

    /**
     * Creates a link between a stone and a Chinese zodiac sign.
     */
    suspend fun insertChineseZodiacCrossRef(crossRef: StoneChineseZodiacCrossRef)

    /**
     * Deletes a link between a stone and a benefit.
     */
    suspend fun deleteBenefitCrossRef(crossRef: StoneBenefitCrossRef)

    // ... (Add other delete/update methods here) ...
}