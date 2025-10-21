package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.wrappers.StoneWithDetails
import com.example.soulstone.domain.model.Stone

@Dao
interface StoneDao {

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: StoneTranslation)

    @Delete
    suspend fun deleteStone(stone: Stone)
}
