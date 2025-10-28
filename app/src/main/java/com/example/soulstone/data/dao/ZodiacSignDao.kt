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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZodiacSign(sign: ZodiacSign)

    @Update
    suspend fun updateZodiacSign(sign: ZodiacSign)

    @Delete
    suspend fun deleteZodiacSign(sign: ZodiacSign)

    @Query("SELECT * FROM zodiac_signs WHERE id = :signId")
    suspend fun getZodiacSignById(signId: Int): ZodiacSign?

    @Query("SELECT * FROM zodiac_signs ORDER BY name ASC")
    fun getAllZodiacSigns(): Flow<List<ZodiacSign>>

    @Query("SELECT * FROM zodiac_signs WHERE name = :signName LIMIT 1")
    suspend fun getZodiacSignByName(signName: String): ZodiacSign?

}