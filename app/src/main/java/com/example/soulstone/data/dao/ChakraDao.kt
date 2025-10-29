package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChakra
import kotlinx.coroutines.flow.Flow

@Dao
interface ChakraDao {

    // --- Main App Queries (using JOINs) ---
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
    fun getTranslatedChakraFlow(sanskritName: String, language: LanguageCode): Flow<TranslatedChakra?>


    // --- Admin/Helper Queries for Chakra table ---
    @Insert
    suspend fun insertChakra(chakra: Chakra): Long?

    @Delete
    suspend fun deleteChakra(chakra: Chakra): Int

    @Query("SELECT * FROM chakras WHERE sanskritName = :sanskritName LIMIT 1")
    suspend fun findChakraBySanskritName(sanskritName: String): Chakra?


    // --- Admin/Helper Queries for ChakraTranslation table ---

    @Insert
    suspend fun insertTranslations(translations: List<ChakraTranslation>)

    @Update
    suspend fun updateTranslation(translation: ChakraTranslation): Int

    @Transaction
    suspend fun insertChakraWithTranslations(
        chakra: Chakra,
        translations: List<ChakraTranslation>
    ) {
        val newChakraId = insertChakra(chakra)
        if(newChakraId == null) {
            throw IllegalStateException(
                "Failed to insert chakra '${chakra.sanskritName}', it might already exist."
            )
        }
        val translationsWithId = translations.map {
            it.copy(chakraId = newChakraId.toInt())
        }
        insertTranslations(translationsWithId)
    }
}
