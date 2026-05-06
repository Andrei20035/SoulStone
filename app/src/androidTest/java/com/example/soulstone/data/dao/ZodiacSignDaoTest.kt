package com.example.soulstone.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.soulstone.data.DbTest
import com.example.soulstone.data.entities.ZodiacSign
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ZodiacSignDaoTest : DbTest() {

    @Test
    fun insert_update_delete_and_query() = runBlocking {
        val dao = db.zodiacSignDao()

        dao.insertZodiacSign(ZodiacSign(name = "Aries"))
        dao.insertZodiacSign(ZodiacSign(name = "Taurus"))

        var all = dao.getAllZodiacSigns().first()
        assertEquals(2, all.size)

        val first = all.first()
        val byId = dao.getZodiacSignById(first.id)
        assertEquals(first, byId)

        val updated = first.copy(name = "Updated")
        dao.updateZodiacSign(updated)
        assertEquals("Updated", dao.getZodiacSignById(first.id)?.name)

        dao.deleteZodiacSign(updated)
        assertNull(dao.getZodiacSignById(first.id))
        all = dao.getAllZodiacSigns().first()
        assertEquals(1, all.size)
    }
}