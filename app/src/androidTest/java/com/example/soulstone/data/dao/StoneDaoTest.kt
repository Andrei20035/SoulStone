package com.example.soulstone.data.dao

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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoneDaoTest : DbTest() {

    @Test
    fun insertFullStone_and_query_back() = runBlocking {
        db.benefitDao().insertBenefit(Benefit(name = "Calming")) // id=1
        db.benefitDao().insertBenefit(Benefit(name = "Focus")) // id=2
        db.chakraDao().insertChakra(Chakra(name = "Root")) // id=1
        db.zodiacSignDao().insertZodiacSign(ZodiacSign(name = "Aries")) // id=1
        db.chineseZodiacSignDao().insertChineseZodiacSign(ChineseZodiacSign(name = "Dragon")) // id=1

        val stone = Stone(imageUri = "content://image/stone1")
        val translations = listOf(
            StoneTranslation(stoneId = 0, languageCode = LanguageCode.ENGLISH, name = "Quartz", description = "Clear quartz"),
            StoneTranslation(stoneId = 0, languageCode = LanguageCode.SPANISH, name = "Cuarzo", description = "Cuarzo transparente")
        )

        // Act
        db.stoneDao().insertFullStone(
            stone = stone,
            translations = translations,
            benefitIds = listOf(1, 2),
            chakraIds = listOf(1),
            zodiacSignIds = listOf(1),
            chineseZodiacSignIds = listOf(1)
        )

        // Assert: fetch all and verify details populated
        val all = db.stoneDao().getAllStones()
        assertEquals(1, all.size)
        val withDetails = all.first()
        assertNotNull(withDetails.stone)
        assertEquals(2, withDetails.translations.size)
        assertEquals(2, withDetails.benefits.size)
        assertEquals(1, withDetails.chakras.size)
        assertEquals(1, withDetails.zodiacSigns.size)
        assertEquals(1, withDetails.chineseZodiacSigns.size)

        val byId = db.stoneDao().getStoneById(withDetails.stone.id)
        assertNotNull(byId)
        val searchResult = db.stoneDao().searchStones(LanguageCode.ENGLISH.code, "Quartz")
        assertTrue(searchResult.isNotEmpty())
    }

    @Test
    fun insert_update_delete_translation() = runBlocking {
        val stoneId = db.stoneDao().insertStone(Stone()).toInt()
        val tr = StoneTranslation(stoneId = stoneId, languageCode = LanguageCode.ENGLISH, name = "A", description = "B")
        db.stoneDao().insertTranslation(tr)

        var all = db.stoneDao().getAllStones()
        assertEquals(1, all.first().translations.size)

        val translationFromDb = all.first().translations.first()

        val updated = translationFromDb.copy(name = "Updated")

        db.stoneDao().updateTranslation(updated)

        all = db.stoneDao().getAllStones()
        assertEquals("Updated", all.first().translations.first().name)

        db.stoneDao().deleteStone(all.first().stone)
        all = db.stoneDao().getAllStones()
        assertTrue(all.isEmpty())
    }
}