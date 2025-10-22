package com.example.soulstone.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.soulstone.data.DbTest
import com.example.soulstone.data.entities.ChineseZodiacSign
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChineseZodiacSignDaoTest : DbTest() {

    @Test
    fun insert_update_delete_and_query() = runBlocking {
        val dao = db.chineseZodiacSignDao()

        dao.insertChineseZodiacSign(ChineseZodiacSign(name = "Dragon"))
        dao.insertChineseZodiacSign(ChineseZodiacSign(name = "Dog"))

        var all = dao.getAllChineseZodiacSigns().first()
        assertEquals(2, all.size)

        val first = all.first()
        val byId = dao.getChineseZodiacSignById(first.id)
        assertEquals(first, byId)

        val updated = first.copy(name = "Updated")
        dao.updateChineseZodiacSign(updated)
        assertEquals("Updated", dao.getChineseZodiacSignById(first.id)?.name)

        dao.deleteChineseZodiacSign(updated)
        assertNull(dao.getChineseZodiacSignById(first.id))
        all = dao.getAllChineseZodiacSigns().first()
        assertEquals(1, all.size)
    }
}