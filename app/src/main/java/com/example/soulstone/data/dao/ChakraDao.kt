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
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChakra
import com.example.soulstone.data.relations.StoneChakraCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ChakraDao {

    // --- Main App Queries (using JOINs) ---
    @Query("""
        SELECT 
            c.id, 
            c.sanskritName, 
            c.iconResId,
            t.name,
            t.description,
            t.location,
            t.ruling_planet AS rulingPlanet,
            t.element,
            t.stone_colors AS stoneColors,
            t.healing_qualities AS healingQualities,
            t.stones,
            t.body_placement AS bodyPlacement,
            t.house_placement AS housePlacement,
            t.herbs,
            t.essential_oils AS essentialOils
        FROM chakras AS c
        JOIN chakra_translations AS t ON c.id = t.chakraId
        WHERE t.languageCode = :language
        ORDER BY c.id ASC
    """)
    fun getAllTranslatedChakras(language: LanguageCode): Flow<List<TranslatedChakra>>

    @Query("""
        SELECT 
            c.id, 
            c.sanskritName, 
            c.iconResId,
            t.name,
            t.description,
            t.location,
            t.ruling_planet AS rulingPlanet,
            t.element,
            t.stone_colors AS stoneColors,
            t.healing_qualities AS healingQualities,
            t.stones,
            t.body_placement AS bodyPlacement,
            t.house_placement AS housePlacement,
            t.herbs,
            t.essential_oils AS essentialOils
        FROM chakras AS c
        JOIN chakra_translations AS t ON c.id = t.chakraId
        WHERE c.sanskritName = :sanskritName AND t.languageCode = :language
    """)
    suspend fun getTranslatedChakra(sanskritName: String, language: LanguageCode): TranslatedChakra?

    @Query("""
        SELECT 
            c.id, 
            c.sanskritName, 
            c.iconResId,
            t.name,
            t.description,
            t.location,
            t.ruling_planet AS rulingPlanet,
            t.element,
            t.stone_colors AS stoneColors,
            t.healing_qualities AS healingQualities,
            t.stones,
            t.body_placement AS bodyPlacement,
            t.house_placement AS housePlacement,
            t.herbs,
            t.essential_oils AS essentialOils
        FROM chakras AS c
        JOIN chakra_translations AS t ON c.id = t.chakraId
        WHERE c.sanskritName = :sanskritName AND t.languageCode = :language
    """)
    fun getTranslatedChakraFlow(sanskritName: String, language: LanguageCode): Flow<TranslatedChakra?>


    // --- Admin/Helper Queries for Chakra table ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChakra(chakra: Chakra): Long

    @Query("SELECT id FROM chakras WHERE sanskritName = :name LIMIT 1")
    suspend fun getChakraIdByName(name: String): Int?

    @Delete
    suspend fun deleteChakra(chakra: Chakra): Int

    @Query("SELECT * FROM chakras WHERE sanskritName = :sanskritName LIMIT 1")
    suspend fun findChakraBySanskritName(sanskritName: String): Chakra?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStoneChakraCrossRef(crossRef: StoneChakraCrossRef)


    // --- Admin/Helper Queries for ChakraTranslation table ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChakraTranslations(translations: List<ChakraTranslation>)

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
        insertChakraTranslations(translationsWithId)
    }
}
