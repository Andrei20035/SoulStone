package com.example.soulstone.data.repository

import android.net.Uri
import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.pojos.StoneWithDetails
import com.example.soulstone.data.storage.FileManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Ensures only one instance of the repository exists
class StoneRepositoryImpl @Inject constructor(
    private val stoneDao: StoneDao,
    private val fileManager: FileManager// The DAO is provided by Hilt
) : StoneRepository {

    // --- Main App List Queries ---

    override fun getAllTranslatedStones(language: LanguageCode): Flow<List<TranslatedStone>> {
        return stoneDao.getAllTranslatedStones(language)
    }

    override fun getStonesForBenefit(benefitKeyName: String, language: LanguageCode): Flow<List<TranslatedStone>> {
        return stoneDao.getStonesForBenefit(benefitKeyName, language)
    }

    override fun getStonesForChakra(chakraSanskritName: String, language: LanguageCode): Flow<List<TranslatedStone>> {
        return stoneDao.getStonesForChakra(chakraSanskritName, language)
    }


    // --- Detail Screen Query ---

    override suspend fun getStoneDetails(keyName: String): StoneWithDetails? {
        return stoneDao.getStoneDetails(keyName)
    }

    override suspend fun getTranslatedStone(keyName: String, language: LanguageCode): TranslatedStone? {
        return stoneDao.getTranslatedStone(keyName, language)
    }


    // --- Admin/Write Operations ---

    override suspend fun createStone(name: String, tempImageUri: Uri) {
        val permanentUriString: String? = try {
            fileManager.saveImageToInternalStorage(tempImageUri)
        } catch (e: Exception) {
            // Handle error: e.g., couldn't copy the file
            null
        }

        val newStone = Stone(
            name = name,
            imageUri = permanentUriString
        )
        stoneDao.insertStone(newStone)
    }

    override suspend fun insertStone(stone: Stone) {
        stoneDao.insertStone(stone)
    }

    override suspend fun insertTranslation(translation: StoneTranslation) {
        stoneDao.insertTranslation(translation)
    }

    override suspend fun insertBenefitCrossRef(crossRef: StoneBenefitCrossRef) {
        stoneDao.insertBenefitCrossRef(crossRef)
    }

    override suspend fun insertChakraCrossRef(crossRef: StoneChakraCrossRef) {
        stoneDao.insertChakraCrossRef(crossRef)
    }

    override suspend fun insertZodiacCrossRef(crossRef: StoneZodiacCrossRef) {
        stoneDao.insertZodiacCrossRef(crossRef)
    }

    override suspend fun insertChineseZodiacCrossRef(crossRef: StoneChineseZodiacCrossRef) {
        stoneDao.insertChineseZodiacCrossRef(crossRef)
    }

    override suspend fun deleteBenefitCrossRef(crossRef: StoneBenefitCrossRef) {
        stoneDao.deleteBenefitCrossRef(crossRef)
    }
}
