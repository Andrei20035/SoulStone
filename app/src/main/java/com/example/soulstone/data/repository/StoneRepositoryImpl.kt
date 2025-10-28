package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import javax.inject.Inject

class StoneRepositoryImpl @Inject constructor(
    private val stoneDao: StoneDao
) : StoneRepository {

    override suspend fun getAllStones() = stoneDao.getAllStones()

    override suspend fun getStoneById(id: Int) = stoneDao.getStoneById(id)

    override suspend fun search(lang: String, query: String) =
        stoneDao.searchStones(lang, query)

    override suspend fun insertFullStone(
        stone: Stone,
        translations: List<StoneTranslation>,
        benefitIds: List<Int>,
        chakraIds: List<Int>,
        zodiacSignIds: List<Int>,
        chineseZodiacSignIds: List<Int>
    ) = stoneDao.insertFullStone(
        stone,
        translations,
        benefitIds,
        chakraIds,
        zodiacSignIds,
        chineseZodiacSignIds
    )

    override suspend fun insertTranslation(translation: StoneTranslation) =
        stoneDao.insertTranslation(translation)

    override suspend fun deleteStone(stone: Stone) =
        stoneDao.deleteStone(stone)
}
