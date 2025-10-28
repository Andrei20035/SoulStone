package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.domain.model.ZodiacSignEnum
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the repository that provides Zodiac sign data.
 * This now mirrors the operations available in ZodiacSignDao.
 */
interface ZodiacRepository {
    /**
     * Fetches the details for a specific sign from the database.
     */
    suspend fun getSignDetails(sign: ZodiacSignEnum): ZodiacSign

    /**
     * Inserts a new zodiac sign into the database.
     */
    suspend fun insertZodiacSign(sign: ZodiacSign)

    /**
     * Updates an existing zodiac sign in the database.
     */
    suspend fun updateZodiacSign(sign: ZodiacSign)

    /**
     * Deletes a zodiac sign from the database.
     */
    suspend fun deleteZodiacSign(sign: ZodiacSign)

    /**
     * Retrieves a single zodiac sign by its ID.
     * Note: This is different from getSignDetails, which uses the enum.
     */
    suspend fun getZodiacSignById(signId: Int): ZodiacSign?

    /**
     * Retrieves all zodiac signs from the database.
     * Using Flow ensures the UI auto-updates.
     */
    fun getAllZodiacSigns(): Flow<List<ZodiacSign>>

    suspend fun getZodiacSignByName(signName: String): ZodiacSign?
}