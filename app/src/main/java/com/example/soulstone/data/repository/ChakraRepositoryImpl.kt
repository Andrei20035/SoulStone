package com.example.soulstone.data.repository

import com.example.soulstone.data.dao.ChakraDao
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChakra
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChakraRepositoryImpl @Inject constructor(
    private val dao: ChakraDao
): ChakraRepository {
    override fun getAllTranslatedChakras(language: LanguageCode): Flow<List<TranslatedChakra>> {
        return dao.getAllTranslatedChakras(language)
    }

    override suspend fun getTranslatedChakra(
        sanskritName: String,
        language: LanguageCode
    ): TranslatedChakra? {
        return dao.getTranslatedChakra(sanskritName, language)
    }

    override fun getTranslatedChakraFlow(
        sanskritName: String,
        language: LanguageCode
    ): Flow<TranslatedChakra?> {
        return dao.getTranslatedChakraFlow(sanskritName, language)
    }

    override suspend fun insertChakra(chakra: Chakra): Long? {
        val existingChakra = dao.findChakraBySanskritName(chakra.sanskritName)

        if(existingChakra == null) {
            return dao.insertChakra(chakra)
        } else {
            return existingChakra.id.toLong()
        }
    }

    override suspend fun deleteChakra(chakra: Chakra): Int {
        return dao.deleteChakra(chakra)
    }

    override suspend fun findChakraBySanskritName(sanskritName: String): Chakra? {
        return dao.findChakraBySanskritName(sanskritName)
    }

    override suspend fun insertTranslations(translations: List<ChakraTranslation>) {
        return dao.insertTranslations(translations)
    }

    override suspend fun updateTranslation(translation: ChakraTranslation): Int {
        return dao.updateTranslation(translation)
    }

    override suspend fun insertChakraWithTranslations(
        chakra: Chakra,
        translations: List<ChakraTranslation>
    ) {
        return dao.insertChakraWithTranslations(chakra, translations)
    }

}