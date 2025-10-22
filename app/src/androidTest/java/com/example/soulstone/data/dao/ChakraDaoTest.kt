package com.example.soulstone.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.soulstone.data.DbTest
import com.example.soulstone.data.entities.Chakra
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChakraDaoTest : DbTest() {

    @Test
    fun insert_update_delete_and_query() = runBlocking {
        val dao = db.chakraDao()

        dao.insertChakra(Chakra(name = "Root"))
        dao.insertChakra(Chakra(name = "Heart"))

        var all = dao.getAllChakras().first()
        assertEquals(2, all.size)

        val first = all.first()
        val byId = dao.getChakraById(first.id)
        assertEquals(first, byId)

        val updated = first.copy(name = "Base")
        dao.updateChakra(updated)
        assertEquals("Base", dao.getChakraById(first.id)?.name)

        dao.deleteChakra(updated)
        assertNull(dao.getChakraById(first.id))
        all = dao.getAllChakras().first()
        assertEquals(1, all.size)
    }
}