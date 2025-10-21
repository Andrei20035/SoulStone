package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.domain.model.Stone

class StoneRepository(private val stoneDao: StoneDao) {

    suspend fun getAllStones() = stoneDao.getAllStones()

    suspend fun getStoneById(id: Int) = stoneDao.getStoneById(id)

    suspend fun search(lang: String, query: String) = stoneDao.searchStones(lang, query)

    suspend fun insertStone(stone: Stone, translations: List<StoneTranslation>) {
        val id = stoneDao.insertStone(stone).toInt()
        translations.forEach { stoneDao.insertTranslation(it.copy(stoneId = id)) }
    }

    suspend fun deleteStone(stone: Stone) = stoneDao.deleteStone(stone)
}
