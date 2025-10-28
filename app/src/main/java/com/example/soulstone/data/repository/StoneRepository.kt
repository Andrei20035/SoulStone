package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation

interface StoneRepository {
    suspend fun getAllStones(): List<Stone>
    suspend fun getStoneById(id: Int): Stone?
    suspend fun search(lang: String, query: String): List<Stone>
    suspend fun insertFullStone(
        stone: Stone,
        translations: List<StoneTranslation>,
        benefitIds: List<Int>,
        chakraIds: List<Int>,
        zodiacSignIds: List<Int>,
        chineseZodiacSignIds: List<Int>
    )
    suspend fun insertTranslation(translation: StoneTranslation)
    suspend fun deleteStone(stone: Stone)
}