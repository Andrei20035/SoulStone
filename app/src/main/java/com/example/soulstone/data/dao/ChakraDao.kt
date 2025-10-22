package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.Chakra
import kotlinx.coroutines.flow.Flow

@Dao
interface ChakraDao {

    /**
     * Inserts a new chakra into the table.
     * If a chakra with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChakra(chakra: Chakra)

    /**
     * Updates an existing chakra.
     * This is what you'll use to "rename" it.
     * Room finds the chakra by its 'id'.
     */
    @Update
    suspend fun updateChakra(chakra: Chakra)

    /**
     * Deletes a chakra from the table.
     * Room finds the chakra by its 'id'.
     */
    @Delete
    suspend fun deleteChakra(chakra: Chakra)

    /**
     * Retrieves a single chakra by its ID.
     */
    @Query("SELECT * FROM chakras WHERE id = :chakraId")
    suspend fun getChakraById(chakraId: Int): Chakra?

    /**
     * Retrieves all chakras from the table, ordered by name.
     * Using Flow ensures your UI will automatically update when the data changes.
     */
    @Query("SELECT * FROM chakras ORDER BY name ASC")
    fun getAllChakras(): Flow<List<Chakra>>
}