package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.ZodiacSign
import kotlinx.coroutines.flow.Flow

@Dao
interface ZodiacSignDao {

    /**
     * Inserts a new zodiac sign into the table.
     * If a sign with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZodiacSign(sign: ZodiacSign)

    /**
     * Updates an existing zodiac sign.
     * This is what you'll use to "rename" it.
     * Room finds the sign by its 'id'.
     */
    @Update
    suspend fun updateZodiacSign(sign: ZodiacSign)

    /**
     * Deletes a zodiac sign from the table.
     * Room finds the sign by its 'id'.
     */
    @Delete
    suspend fun deleteZodiacSign(sign: ZodiacSign)

    /**
     * Retrieves a single zodiac sign by its ID.
     */
    @Query("SELECT * FROM zodiac_signs WHERE id = :signId")
    suspend fun getZodiacSignById(signId: Int): ZodiacSign?

    /**
     * Retrieves all zodiac signs from the table, ordered by name.
     * Using Flow ensures your UI will automatically update when the data changes.
     */
    @Query("SELECT * FROM zodiac_signs ORDER BY name ASC")
    fun getAllZodiacSigns(): Flow<List<ZodiacSign>>
}