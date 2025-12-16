package com.example.soulstone.data.repository

import android.net.Uri
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.pojos.StoneInventoryView
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.pojos.StoneWithDetails
import com.example.soulstone.ui.models.StoneGridItem
import kotlinx.coroutines.flow.Flow

interface StoneRepository {
    fun getAllTranslatedStones(language: LanguageCode): Flow<List<TranslatedStone>>
    fun getStonesForBenefit(benefitId: Int, language: LanguageCode): Flow<List<TranslatedStone>>
    fun getStonesForChakraFlow(chakraSanskritName: String, language: LanguageCode, limit: Int): Flow<List<StoneListItem>>
    fun getStonesForSignFlow(keyName: String, language: LanguageCode, limit: Int): Flow<List<StoneListItem>>
    fun getAllStonesForIndex(language: LanguageCode): Flow<List<StoneListItem>>

    fun getAllStonesInventory(language: LanguageCode): Flow<List<StoneInventoryView>>

    suspend fun updateStoneDescription(stoneId: Int, newDescription: String, languageCode: LanguageCode)

    fun getStonesForChineseSignFlow(keyName: String, languageCode: LanguageCode, limit: Int): Flow<List<StoneListItem>>

    suspend fun getStoneDetails(keyName: String): StoneWithDetails?
    suspend fun getTranslatedStoneFlow(stoneId: Int, language: LanguageCode): Flow<TranslatedStone?>
    suspend fun insertStone(stone: Stone): Long
    suspend fun insertTranslation(translation: StoneTranslation)
    suspend fun insertBenefitCrossRef(crossRef: StoneBenefitCrossRef)
    suspend fun insertChakraCrossRef(crossRef: StoneChakraCrossRef)
    suspend fun insertZodiacCrossRef(crossRef: StoneZodiacCrossRef)
    suspend fun insertChineseZodiacCrossRef(crossRef: StoneChineseZodiacCrossRef)
    suspend fun deleteBenefitCrossRef(crossRef: StoneBenefitCrossRef)
    suspend fun createStone(name: String, tempImageUri: Uri)

}