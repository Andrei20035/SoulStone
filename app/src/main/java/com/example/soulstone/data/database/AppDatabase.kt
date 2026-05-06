package com.example.soulstone.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.soulstone.data.AppInitializationState
import com.example.soulstone.data.converters.Converters
import com.example.soulstone.data.dao.BenefitDao
import com.example.soulstone.data.dao.ChakraDao
import com.example.soulstone.data.dao.ChineseZodiacSignDao
import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.dao.ZodiacSignDao
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.BenefitTranslation
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChakraTranslation
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.ChineseZodiacSignTranslation
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.entities.ZodiacSignTranslation
import com.example.soulstone.data.models.BenefitAssociationJsonItem
import com.example.soulstone.data.models.BenefitJsonItem
import com.example.soulstone.data.models.ChakraAssociationJsonItem
import com.example.soulstone.data.models.ChakraJsonItem
import com.example.soulstone.data.models.ZodiacStoneAssociationJsonItem
import com.example.soulstone.data.models.ChineseZodiacJsonItem
import com.example.soulstone.data.models.StoneJsonItem
import com.example.soulstone.data.models.ZodiacJsonItem
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.util.LanguageCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        Stone::class,
        StoneTranslation::class,
        ZodiacSign::class,
        ZodiacSignTranslation::class,
        ChineseZodiacSign::class,
        ChineseZodiacSignTranslation::class,
        Benefit::class,
        BenefitTranslation::class,
        Chakra::class,
        ChakraTranslation::class,
        StoneZodiacCrossRef::class,
        StoneChineseZodiacCrossRef::class,
        StoneBenefitCrossRef::class,
        StoneChakraCrossRef::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stoneDao(): StoneDao
    abstract fun benefitDao(): BenefitDao
    abstract fun chakraDao(): ChakraDao
    abstract fun zodiacSignDao(): ZodiacSignDao
    abstract fun chineseZodiacSignDao(): ChineseZodiacSignDao

    class ZodiacDatabaseCallback @Inject constructor(
        private val context: Context,
        private val dbProvider: Provider<AppDatabase>,
        private val appInitState: AppInitializationState
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "onCreate called — populating database")

            // Launch ONE coroutine to ensure sequential execution
            CoroutineScope(Dispatchers.IO).launch {
                val database = dbProvider.get()

                // 1. Populate Independent Tables
                populateWesternZodiacs(database.zodiacSignDao())
                populateBenefits(database.benefitDao())
                populateChakras(database.chakraDao())

                // 2. Populate Tables needed for Linking
                populateChineseZodiacs(database.chineseZodiacSignDao())
                populateStones(database.stoneDao())

                // 3. Link Tables (Dependent on Step 2)
                linkStonesToChineseZodiacs(database.stoneDao(), database.chineseZodiacSignDao())
                linkStonesToWesternZodiacs(database.stoneDao(), database.zodiacSignDao())
                linkStonesToChakras(database.stoneDao(), database.chakraDao())
                linkStonesToBenefits(database.stoneDao(), database.benefitDao())

                Log.d("AppDatabase", "Database population finished.")
                appInitState.markDatabaseAsReady()
            }
        }

        private suspend fun populateWesternZodiacs(dao: ZodiacSignDao) {
            try {
                Log.d("AppDatabase", "Populating Western Zodiacs...")
                val jsonString = context.assets.open("initial_zodiac_signs.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<ZodiacJsonItem>>() {}.type
                val zodiacList: List<ZodiacJsonItem> = Gson().fromJson(jsonString, listType)

                zodiacList.forEach { item ->
                    val sign = ZodiacSign(
                        name = item.key,
                        startDate = item.startDate,
                        endDate = item.endDate,
                        imageName = item.imageName
                    )

                    val translations = item.translations.map { (lang, data) ->
                        ZodiacSignTranslation(
                            zodiacSignId = 0,
                            languageCode = mapLanguageCode(lang),
                            name = data.name,
                            description = data.description,
                            element = data.element,
                            rulingPlanet = data.planet
                        )
                    }
                    dao.insertZodiacSignWithTranslations(sign, translations)
                }
                Log.d("AppDatabase", "Western Zodiacs populated.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Zodiac JSON", e)
            }
        }

        private suspend fun populateBenefits(dao: BenefitDao) {
            try {
                Log.d("AppDatabase", "Populating Benefits...")
                val jsonString = context.assets.open("initial_benefits.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<BenefitJsonItem>>() {}.type
                val benefitList: List<BenefitJsonItem> = Gson().fromJson(jsonString, listType)

                benefitList.forEach { item ->
                    val benefit = Benefit(
                        name = item.key,
                        imageName = item.imageName
                    )

                    val translations = item.translations.map { (lang, data) ->
                        BenefitTranslation(
                            benefitId = 0,
                            languageCode = mapLanguageCode(lang),
                            name = data.name
                        )
                    }
                    dao.insertBenefitWithTranslations(benefit, translations)
                }
                Log.d("AppDatabase", "Benefits populated.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Benefits JSON", e)
            }
        }

        private suspend fun populateChineseZodiacs(dao: ChineseZodiacSignDao) {
            try {
                Log.d("AppDatabase", "Populating Chinese Zodiacs...")
                val jsonString = context.assets.open("initial_chinese_zodiac.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<ChineseZodiacJsonItem>>() {}.type
                val list: List<ChineseZodiacJsonItem> = Gson().fromJson(jsonString, listType)

                list.forEach { item ->
                    val baseName = item.imageBase

                    val sign = ChineseZodiacSign(
                        keyName = item.key,
                        iconName = baseName,
                        iconBorderName = "${baseName}_border",
                        iconColorName = "${baseName}_color",
                        recentYears = item.years
                    )

                    var parentId = dao.insertChineseSign(sign)

                    if (parentId == -1L) {
                        val existingId = dao.getChineseSignIdByKey(item.key)
                        if (existingId != null) parentId = existingId.toLong()
                        else return@forEach
                    }

                    val translations = item.translations.map { (lang, data) ->
                        ChineseZodiacSignTranslation(
                            chineseSignId = parentId.toInt(),
                            languageCode = mapLanguageCode(lang),
                            name = data.name,
                            description = data.description,
                            traits = data.traits,
                            bestMatch = data.bestMatch,
                            worstMatch = data.worstMatch,
                            compatibilityDescription = data.compatibilityDesc,
                            gemstoneDescription = data.gemstoneDesc
                        )
                    }
                    dao.insertChineseTranslations(translations)
                }
                Log.d("AppDatabase", "Chinese Zodiacs populated.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Chinese Zodiac JSON", e)
            }
        }

        private suspend fun populateStones(dao: StoneDao) {
            try {
                Log.d("AppDatabase", "Populating Stones...")
                val jsonString = context.assets.open("initial_stones.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<StoneJsonItem>>() {}.type
                val stoneList: List<StoneJsonItem> = Gson().fromJson(jsonString, listType)

                val exceptionsMap = mapOf(
                    "jade" to "green_jade",
                    "moonstone" to "moon_stone",
                )

                stoneList.forEach { item ->
                    val drawableName = exceptionsMap[item.imageName] ?: item.imageName

                    val stone = Stone(name = item.imageName, imageUri = drawableName)
                    val translations = item.translations.map { (lang, data) ->
                        StoneTranslation(
                            stoneId = 0,
                            languageCode = mapLanguageCode(lang),
                            name = data.name,
                            description = data.description
                        )
                    }
                    dao.insertStoneWithTranslations(stone, translations)
                }
                Log.d("AppDatabase", "Stones populated.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Stone JSON", e)
            }
        }

        private suspend fun populateChakras(dao: ChakraDao) {
            try {
                Log.d("AppDatabase", "Populating Chakras...")
                val jsonString = context.assets.open("initial_chakras.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<ChakraJsonItem>>() {}.type
                val list: List<ChakraJsonItem> = Gson().fromJson(jsonString, listType)


                list.forEach { item ->
                    val chakra = Chakra(
                        sanskritName = item.key,
                        imageName = item.imageBase
                    )

                    var parentId = dao.insertChakra(chakra)
                    if (parentId == -1L) {
                        val existingId = dao.getChakraIdByName(item.key)
                        if (existingId != null) parentId = existingId.toLong()
                        else return@forEach
                    }

                    val translations = item.translations.map { (lang, data) ->
                        ChakraTranslation(
                            chakraId = parentId.toInt(),
                            languageCode = mapLanguageCode(lang),
                            name = data.name,
                            description = data.description,
                            location = data.location,
                            rulingPlanet = data.rulingPlanet,
                            element = data.element,
                            stoneColors = data.stoneColors,
                            healingQualities = data.healingQualities,
                            stones = data.stones,
                            bodyPlacement = data.bodyPlacement,
                            housePlacement = data.housePlacement,
                            herbs = data.herbs,
                            essentialOils = data.essentialOils
                        )
                    }
                    dao.insertChakraTranslations(translations)
                }
                Log.d("AppDatabase", "Chakras populated.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Chakra JSON", e)
            }
        }

        private suspend fun linkStonesToChineseZodiacs(stoneDao: StoneDao, chineseDao: ChineseZodiacSignDao) {
            try {
                Log.d("AppDatabase", "Linking Stones to Chinese Zodiacs...")
                val jsonString = context.assets.open("initial_chinese_associations.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<ZodiacStoneAssociationJsonItem>>() {}.type
                val list: List<ZodiacStoneAssociationJsonItem> = Gson().fromJson(jsonString, listType)

                list.forEach { item ->
                    val zodiacId = chineseDao.getChineseSignIdByKey(item.zodiacKey)

                    if (zodiacId != null) {
                        item.stoneKeys.forEach { stoneKey ->
                            val stoneId = stoneDao.getStoneIdByKey(stoneKey)
                            if (stoneId != null) {
                                stoneDao.insertChineseZodiacCrossRef(
                                    StoneChineseZodiacCrossRef(stoneId = stoneId, chineseZodiacSignId = zodiacId)
                                )
                            } else {
                                Log.w("AppDatabase", "Skip Link: Stone '$stoneKey' not found.")
                            }
                        }
                    } else {
                        Log.w("AppDatabase", "Skip Link: Zodiac '${item.zodiacKey}' not found.")
                    }
                }
                Log.d("AppDatabase", "Linking finished.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error linking Chinese Zodiacs", e)
            }
        }



        private suspend fun linkStonesToWesternZodiacs(stoneDao: StoneDao, zodiacDao: ZodiacSignDao) {
            try {
                Log.d("AppDatabase", "Linking Stones to Western Zodiacs...")
                val jsonString = context.assets.open("initial_zodiac_associations.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<ZodiacStoneAssociationJsonItem>>() {}.type
                val list: List<ZodiacStoneAssociationJsonItem> = Gson().fromJson(jsonString, listType)

                list.forEach { item ->
                    val zodiacId = zodiacDao.getZodiacSignIdByKey(item.zodiacKey)

                    if (zodiacId != null) {
                        item.stoneKeys.forEach { stoneKey ->
                            val stoneId = stoneDao.getStoneIdByKey(stoneKey)

                            if (stoneId != null) {
                                stoneDao.insertZodiacCrossRef(
                                    StoneZodiacCrossRef(stoneId = stoneId, zodiacSignId = zodiacId)
                                )
                            } else {
                                Log.w("AppDatabase", "Skip Link: Stone '$stoneKey' not found.")
                            }
                        }
                    } else {
                        Log.w("AppDatabase", "Skip Link: Zodiac '${item.zodiacKey}' not found.")
                    }
                }
                Log.d("AppDatabase", "Western Linking finished.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error linking Western Zodiacs", e)
            }
        }

        private suspend fun linkStonesToChakras(stoneDao: StoneDao, chakraDao: ChakraDao) {
            try {
                Log.d("AppDatabase", "Linking Stones to Chakras...")
                val jsonString = context.assets.open("initial_chakra_associations.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<ChakraAssociationJsonItem>>() {}.type
                val list: List<ChakraAssociationJsonItem> = Gson().fromJson(jsonString, listType)

                list.forEach { item ->
                    val chakraId = chakraDao.getChakraIdByName(item.chakraKey)

                    if (chakraId != null) {
                        item.stoneKeys.forEach { stoneKey ->
                            val stoneId = stoneDao.getStoneIdByKey(stoneKey)

                            if (stoneId != null) {
                                chakraDao.insertStoneChakraCrossRef(
                                    StoneChakraCrossRef(stoneId = stoneId, chakraId = chakraId)
                                )
                            } else {
                                Log.w("AppDatabase", "Skip Link: Stone '$stoneKey' not found.")
                            }
                        }
                    } else {
                        Log.w("AppDatabase", "Skip Link: Chakra '${item.chakraKey}' not found.")
                    }
                }
                Log.d("AppDatabase", "Chakra linking finished.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error linking Chakras", e)
            }
        }

        private suspend fun linkStonesToBenefits(stoneDao: StoneDao, benefitDao: BenefitDao) {
            try {
                Log.d("AppDatabase", "Linking Stones to Benefits...")
                val jsonString = context.assets.open("initial_benefit_associations.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<BenefitAssociationJsonItem>>() {}.type
                val list: List<BenefitAssociationJsonItem> = Gson().fromJson(jsonString, listType)

                list.forEach { item ->
                    val benefitId = benefitDao.getBenefitIdByName(item.benefitKey)

                    if (benefitId != null) {
                        item.stoneKeys.forEach { stoneKey ->
                            // 2. Get Stone ID
                            val stoneId = stoneDao.getStoneIdByKey(stoneKey)

                            if (stoneId != null) {
                                // 3. Insert Link
                                benefitDao.insertStoneBenefitCrossRef(
                                    StoneBenefitCrossRef(stoneId = stoneId, benefitId = benefitId)
                                )
                            } else {
                                Log.w("AppDatabase", "Skip Link: Stone '$stoneKey' not found.")
                            }
                        }
                    } else {
                        Log.w("AppDatabase", "Skip Link: Benefit '${item.benefitKey}' not found.")
                    }
                }
                Log.d("AppDatabase", "Benefit linking finished.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error linking Benefits", e)
            }
        }

        private fun mapLanguageCode(key: String): LanguageCode {
            return when (key.lowercase()) {
                "en" -> LanguageCode.ENGLISH
                "es" -> LanguageCode.SPANISH
                "fr" -> LanguageCode.FRENCH
                "it" -> LanguageCode.ITALIAN
                "de" -> LanguageCode.GERMAN
                "pl" -> LanguageCode.POLISH
                "ru" -> LanguageCode.RUSSIAN
                else -> LanguageCode.ENGLISH
            }
        }
    }
}

