package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.ZodiacSignDao
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.domain.model.ZodiacSignEnum
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Implementation of the ZodiacRepository.
 *
 * This implementation uses @Inject constructor() so Hilt knows how to create it.
 * It depends on the ZodiacSignDao, which will also be injected by Hilt.
 * All methods are simple pass-through calls to the DAO.
 */
class ZodiacRepositoryImpl @Inject constructor(
    private val zodiacSignDao: ZodiacSignDao
) : ZodiacRepository {

    /**
     * Fetches details by the enum.
     * We map the enum to its 'ordinal' (its position in the enum, 0-11)
     * to use as the Int ID for the database query.
     *
     * Throws an exception if the sign isn't found, to satisfy the
     * non-nullable return type.
     */
    override suspend fun getSignDetails(sign: ZodiacSignEnum): ZodiacSign {
        // Use the enum's ordinal (0 for ARIES, 1 for TAURUS, etc.) as the ID
        return zodiacSignDao.getZodiacSignById(sign.ordinal)
            ?: throw IllegalStateException("Zodiac sign ${sign.name} not found in database.")
    }

    override suspend fun insertZodiacSign(sign: ZodiacSign) {
        zodiacSignDao.insertZodiacSign(sign)
    }

    override suspend fun updateZodiacSign(sign: ZodiacSign) {
        zodiacSignDao.updateZodiacSign(sign)
    }

    override suspend fun deleteZodiacSign(sign: ZodiacSign) {
        zodiacSignDao.deleteZodiacSign(sign)
    }

    override suspend fun getZodiacSignById(signId: Int): ZodiacSign? {
        return zodiacSignDao.getZodiacSignById(signId)
    }

    override fun getAllZodiacSigns(): Flow<List<ZodiacSign>> {
        return zodiacSignDao.getAllZodiacSigns()
    }

    override suspend fun getZodiacSignByName(signName: String): ZodiacSign? {
        return zodiacSignDao.getZodiacSignByName(signName)
    }
}

