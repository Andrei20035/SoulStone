package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.data.model.LanguageCode
import com.example.soulstone.data.model.TranslatedChakra
import kotlinx.coroutines.flow.Flow

@Dao
interface ChakraDao {

    // --- Main App Queries (using JOINs) ---

    /**
     * Fetches a list of all chakras, translated into the specified language.
     * This is the main query you'll use to display a list of chakras in the app.
     * They are ordered by ID, which should be (1-7, Root to Crown).
     */
    @Query("""
        SELECT 
            c.id AS id, 
            c.sanskritName AS sanskritName, 
            t.name AS name,
            t.rulingPlanet AS rulingPlanet,
            t.color AS color,
            t.location AS location,
            t.description AS description,
            t.languageCode AS languageCode
        FROM chakras AS c
        JOIN chakra_translations AS t ON c.id = t.chakraId
        WHERE t.languageCode = :language
        ORDER BY c.id ASC
    """)
    fun getAllTranslatedChakras(language: LanguageCode): Flow<List<TranslatedChakra>>

    /**
     * Fetches a single chakra, translated into the specified language,
     * by its stable Sanskrit name.
     */
    @Query("""
        SELECT 
            c.id AS id, 
            c.sanskritName AS sanskritName, 
            t.name AS name,
            t.rulingPlanet AS rulingPlanet,
            t.color AS color,
            t.location AS location,
            t.description AS description,
            t.languageCode AS languageCode
        FROM chakras AS c
        JOIN chakra_translations AS t ON c.id = t.chakraId
        WHERE c.sanskritName = :sanskritName AND t.languageCode = :language
    """)
    suspend fun getTranslatedChakra(sanskritName: String, language: LanguageCode): TranslatedChakra?


    // --- Admin/Helper Queries for Chakra table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChakra(chakra: Chakra)

    @Update
    suspend fun updateChakra(chakra: Chakra)

    @Delete
    suspend fun deleteChakra(chakra: Chakra)

    @Query("SELECT * FROM chakras WHERE sanskritName = :sanskritName LIMIT 1")
    suspend fun getChakraBySanskritName(sanskritName: String): Chakra?


    // --- Admin/Helper Queries for ChakraTranslation table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: ChakraTranslation)

    @Update
    suspend fun updateTranslation(translation: ChakraTranslation)

    @Delete
    suspend fun deleteTranslation(translation: ChakraTranslation)
}
