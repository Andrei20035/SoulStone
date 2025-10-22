package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.ChineseZodiacSign
import kotlinx.coroutines.flow.Flow

@Dao
interface ChineseZodiacSignDao {

    /**
     * Inserts a new Chinese zodiac sign into the table.
     * If a sign with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChineseZodiacSign(sign: ChineseZodiacSign)

    /**
     * Updates an existing Chinese zodiac sign.
     * This is what you'll use to "rename" it.
     * Room finds the sign by its 'id'.
     */
    @Update
    suspend fun updateChineseZodiacSign(sign: ChineseZodiacSign)

    /**
     * Deletes a Chinese zodiac sign from the table.
     * Room finds the sign by its 'id'.
     */
    @Delete
    suspend fun deleteChineseZodiacSign(sign: ChineseZodiacSign)

    /**
     * Retrieves a single Chinese zodiac sign by its ID.
     */
    @Query("SELECT * FROM chinese_zodiac_signs WHERE id = :signId")
    suspend fun getChineseZodiacSignById(signId: Int): ChineseZodiacSign?

    /**
     * Retrieves all Chinese zodiac signs from the table, ordered by name.
     * Using Flow ensures your UI will automatically update when the data changes.
     */
    @Query("SELECT * FROM chinese_zodiac_signs ORDER BY name ASC")
    fun getAllChineseZodiacSigns(): Flow<List<ChineseZodiacSign>>
}