package com.example.soulstone.data.repository

import androidx.room.Transaction
import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef

class StoneRepository(private val stoneDao: StoneDao) {

    suspend fun getAllStones() = stoneDao.getAllStones()

    suspend fun getStoneById(id: Int) = stoneDao.getStoneById(id)

    suspend fun search(lang: String, query: String) = stoneDao.searchStones(lang, query)

    suspend fun insertFullStone(
        stone: Stone,
        translations: List<StoneTranslation>,
        benefitIds: List<Int>,
        chakraIds: List<Int>,
        zodiacSignIds: List<Int>,
        chineseZodiacSignIds: List<Int>
    ) {
        stoneDao.insertFullStone(
            stone,
            translations,
            benefitIds,
            chakraIds,
            zodiacSignIds,
            chineseZodiacSignIds
        )
    }

    suspend fun insertTranslation(translation: StoneTranslation) {
        stoneDao.insertTranslation(translation)
    }

    suspend fun deleteStone(stone: Stone) = stoneDao.deleteStone(stone)
}
