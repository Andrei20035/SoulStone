package com.example.soulstone.data.repository

import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.util.LanguageCode
import com.example.soulstone.data.pojos.TranslatedChakra
import kotlinx.coroutines.flow.Flow

interface ChakraRepository {
    fun getAllTranslatedChakras(language: LanguageCode): Flow<List<TranslatedChakra>>
    suspend fun getTranslatedChakra(sanskritName: String, language: LanguageCode): TranslatedChakra?
    fun getTranslatedChakraFlow(sanskritName: String, language: LanguageCode): Flow<TranslatedChakra?>
    suspend fun insertChakra(chakra: Chakra): Long?
    suspend fun deleteChakra(chakra: Chakra): Int
    suspend fun findChakraBySanskritName(sanskritName: String): Chakra?
    suspend fun insertTranslations(translations: List<ChakraTranslation>)
    suspend fun updateTranslation(translation: ChakraTranslation): Int
    suspend fun insertChakraWithTranslations(chakra: Chakra, translations: List<ChakraTranslation>)
}