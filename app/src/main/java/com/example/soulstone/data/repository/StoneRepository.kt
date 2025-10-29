package com.example.soulstone.data.repository

import android.net.Uri
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedStone
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.data.pojos.StoneWithDetails
import kotlinx.coroutines.flow.Flow

interface StoneRepository {
    fun getAllTranslatedStones(language: LanguageCode): Flow<List<TranslatedStone>>
    fun getStonesForBenefit(benefitKeyName: String, language: LanguageCode): Flow<List<TranslatedStone>>
    fun getStonesForChakra(chakraSanskritName: String, language: LanguageCode): Flow<List<TranslatedStone>>
    suspend fun getStoneDetails(keyName: String): StoneWithDetails?
    suspend fun getTranslatedStone(keyName: String, language: LanguageCode): TranslatedStone?
    suspend fun insertStone(stone: Stone)
    suspend fun insertTranslation(translation: StoneTranslation)
    suspend fun insertBenefitCrossRef(crossRef: StoneBenefitCrossRef)
    suspend fun insertChakraCrossRef(crossRef: StoneChakraCrossRef)
    suspend fun insertZodiacCrossRef(crossRef: StoneZodiacCrossRef)
    suspend fun insertChineseZodiacCrossRef(crossRef: StoneChineseZodiacCrossRef)
    suspend fun deleteBenefitCrossRef(crossRef: StoneBenefitCrossRef)
    suspend fun createStone(name: String, tempImageUri: Uri)

}