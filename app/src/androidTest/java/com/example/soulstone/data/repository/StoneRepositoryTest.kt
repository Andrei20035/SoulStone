package com.example.soulstone.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.soulstone.data.DbTest
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.util.LanguageCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoneRepositoryTest : DbTest() {

    @Test
    fun repository_delegates_and_persists() = runBlocking {
        // Seed lookup tables
        db.benefitDao().insertBenefit(Benefit(name = "Calming"))
        db.chakraDao().insertChakra(Chakra(name = "Root"))
        db.zodiacSignDao().insertZodiacSign(ZodiacSign(name = "Aries"))
        db.chineseZodiacSignDao().insertChineseZodiacSign(ChineseZodiacSign(keyName = "Dragon"))

        val repo = StoneRepositoryImpl(db.stoneDao())
        repo.insertFullStone(
            stone = Stone(imageUri = "content://image/stone2"),
            translations = listOf(
                StoneTranslation(stoneId = 0, languageCode = LanguageCode.ENGLISH, name = "Amethyst", description = "Purple"),
                StoneTranslation(stoneId = 0, languageCode = LanguageCode.POLISH, name = "Ametyst", description = "Fioletowy")
            ),
            benefitIds = listOf(1),
            chakraIds = listOf(1),
            zodiacSignIds = listOf(1),
            chineseZodiacSignIds = listOf(1)
        )

        val all = repo.getAllStones()
        assertEquals(1, all.size)

        val searched = repo.search(LanguageCode.ENGLISH.code, "Amethyst")
        assertTrue(searched.isNotEmpty())

        val id = all.first().stone.id
        val byId = repo.getStoneById(id)
        assertEquals(id, byId?.stone?.id)
    }
}