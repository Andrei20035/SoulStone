package com.example.soulstone.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.soulstone.data.DbTest
import com.example.soulstone.data.entities.Benefit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BenefitDaoTest : DbTest() {

    @Test
    fun insert_update_delete_and_query() = runBlocking {
        val dao = db.benefitDao()

        dao.insertBenefit(Benefit(name = "Calming"))
        dao.insertBenefit(Benefit(name = "Focus"))

        var all = dao.getAllBenefits().first()
        assertEquals(2, all.size)

        val first = all.first()
        val byId = dao.getBenefitById(first.id)
        assertEquals(first, byId)

        val updated = first.copy(name = "Relaxation")
        dao.updateBenefit(updated)
        assertEquals("Relaxation", dao.getBenefitById(first.id)?.name)

        dao.deleteBenefit(updated)
        assertNull(dao.getBenefitById(first.id))
        all = dao.getAllBenefits().first()
        assertEquals(1, all.size)
    }
}