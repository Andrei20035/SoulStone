package com.example.soulstone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.soulstone.data.entities.Benefit
import kotlinx.coroutines.flow.Flow

@Dao
interface BenefitDao {
    /**
     * Inserts a new benefit into the table.
     * If a benefit with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenefit(benefit: Benefit)

    /**
     * Updates an existing benefit.
     * This is what you'll use to "rename" it.
     * Room finds the benefit by its 'id'.
     */
    @Update
    suspend fun updateBenefit(benefit: Benefit)

    /**
     * Deletes a benefit from the table.
     * Room finds the benefit by its 'id'.
     */
    @Delete
    suspend fun deleteBenefit(benefit: Benefit)

    /**
     * Retrieves a single benefit by its ID.
     */
    @Query("SELECT * FROM benefits WHERE id = :benefitId")
    suspend fun getBenefitById(benefitId: Int): Benefit?

    /**
     * Retrieves all benefits from the table, ordered by name.
     * Using Flow ensures your UI will automatically update when the data changes.
     */
    @Query("SELECT * FROM benefits ORDER BY name ASC")
    fun getAllBenefits(): Flow<List<Benefit>>
}