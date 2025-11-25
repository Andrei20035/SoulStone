package com.example.soulstone.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.soulstone.R
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
import com.example.soulstone.data.models.ChineseIcons
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
        private val dbProvider: Provider<AppDatabase>
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
            }
        }

        private suspend fun populateWesternZodiacs(dao: ZodiacSignDao) {
            try {
                Log.d("AppDatabase", "Populating Western Zodiacs...")
                val jsonString = context.assets.open("initial_zodiac_signs.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<ZodiacJsonItem>>() {}.type
                val zodiacList: List<ZodiacJsonItem> = Gson().fromJson(jsonString, listType)

                val drawableMap = mapOf(
                    "aries" to R.drawable.aries,
                    "taurus" to R.drawable.taurus,
                    "gemini" to R.drawable.gemini,
                    "cancer" to R.drawable.cancer,
                    "leo" to R.drawable.leo,
                    "virgo" to R.drawable.virgo,
                    "libra" to R.drawable.libra,
                    "scorpio" to R.drawable.scorpio,
                    "sagittarius" to R.drawable.sagittarius,
                    "capricorn" to R.drawable.capricorn,
                    "aquarius" to R.drawable.aquarius,
                    "pisces" to R.drawable.pisces
                )

                zodiacList.forEach { item ->
                    val icon = drawableMap[item.imageName] ?: R.drawable.aries
                    val sign = ZodiacSign(
                        name = item.key,
                        startDate = item.startDate,
                        endDate = item.endDate,
                        iconResId = icon
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

                val drawableMap = mapOf(
                    "stone_1" to R.drawable.stone_1, "stone_2" to R.drawable.stone_2,
                    "stone_3" to R.drawable.stone_3, "stone_4" to R.drawable.stone_4,
                    "stone_5" to R.drawable.stone_5, "stone_6" to R.drawable.stone_6,
                    "stone_7" to R.drawable.stone_7, "stone_8" to R.drawable.stone_8
                )

                benefitList.forEach { item ->
                    val icon = drawableMap[item.imageName] ?: R.drawable.stone_1
                    val benefit = Benefit(name = item.key, imageResId = icon)
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

                val drawableMap = mapOf(
                    "rat" to ChineseIcons(R.drawable.rat, R.drawable.rat_border, R.drawable.rat_color),
                    "ox" to ChineseIcons(R.drawable.ox, R.drawable.ox_border, R.drawable.ox_color),
                    "tiger" to ChineseIcons(R.drawable.tiger, R.drawable.tiger_border, R.drawable.tiger_color),
                    "rabbit" to ChineseIcons(R.drawable.rabbit, R.drawable.rabbit_border, R.drawable.rabbit_color),
                    "dragon" to ChineseIcons(R.drawable.dragon, R.drawable.dragon_border, R.drawable.dragon_color),
                    "snake" to ChineseIcons(R.drawable.snake, R.drawable.snake_border, R.drawable.snake_color),
                    "horse" to ChineseIcons(R.drawable.horse, R.drawable.horse_border, R.drawable.horse_color),
                    "goat" to ChineseIcons(R.drawable.goat, R.drawable.goat_border, R.drawable.goat_color),
                    "monkey" to ChineseIcons(R.drawable.monkey, R.drawable.monkey_border, R.drawable.monkey_color),
                    "rooster" to ChineseIcons(R.drawable.rooster, R.drawable.rooster_border, R.drawable.rooster_color),
                    "dog" to ChineseIcons(R.drawable.dog, R.drawable.dog_border, R.drawable.dog_color),
                    "pig" to ChineseIcons(R.drawable.pig, R.drawable.pig_border, R.drawable.pig_color)
                )

                list.forEach { item ->
                    val icons = drawableMap[item.imageBase] ?: ChineseIcons(
                        R.drawable.rat,
                        R.drawable.rat_border,
                        R.drawable.rat_color
                    )

                    val sign = ChineseZodiacSign(
                        name = item.key,
                        iconResId = icons.icon,
                        iconResIdBorder = icons.border,
                        iconResIdColor = icons.color,
                        recentYears = item.years
                    )

                    // 1. Insert Parent
                    var parentId = dao.insertChineseSign(sign)

                    // 2. Handle Duplicates
                    if (parentId == -1L) {
                        val existingId = dao.getChineseSignIdByKey(item.key)
                        if (existingId != null) parentId = existingId.toLong()
                        else return@forEach
                    }

                    // 3. Insert Translations using Parent ID
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

                val drawableMap = mapOf(
                    "agate" to R.drawable.agate,
                    "amazonite" to R.drawable.amazonite,
                    "amber" to R.drawable.amber,
                    "amethyst" to R.drawable.amethyst,
                    "angelite" to R.drawable.angelite,
                    "apatite" to R.drawable.apatite,
                    "aquamarine" to R.drawable.aquamarine,
                    "aventurine" to R.drawable.aventurine,
                    "black_obsidian" to R.drawable.black_obsidian,
                    "black_tourmaline" to R.drawable.black_tourmaline,
                    "bloodstone" to R.drawable.bloodstone,
                    "blue_agate" to R.drawable.blue_agate,
                    "blue_calcite" to R.drawable.blue_calcite,
                    "blue_chalcedony" to R.drawable.blue_chalcedony,
                    "blue_kyanite" to R.drawable.blue_kyanite,
                    "bronzite" to R.drawable.bronzite,
                    "carnelian" to R.drawable.carnelian,
                    "chrysocolla" to R.drawable.chrysocolla,
                    "chrysoprase" to R.drawable.chrysoprase,
                    "citrine" to R.drawable.citrine,
                    "clear_quartz" to R.drawable.clear_quartz,
                    "coral" to R.drawable.coral,
                    "dalmatian_jasper" to R.drawable.dalmatian_jasper,
                    "diamond" to R.drawable.diamond,
                    "emerald" to R.drawable.emerald,
                    "fluorite" to R.drawable.fluorite,
                    "golden_chalcedony" to R.drawable.golden_chalcedony,
                    "green_aventurine" to R.drawable.green_aventurine,
                    "green_jasper" to R.drawable.green_jasper,
                    "green_quartz" to R.drawable.green_quartz,
                    "hawk_eye" to R.drawable.hawk_eye,
                    "hematite" to R.drawable.hematite,
                    "howlite" to R.drawable.howlite,
                    "jade" to R.drawable.green_jade,
                    "jasper" to R.drawable.jasper,
                    "labradorite" to R.drawable.labradorite,
                    "lapis_lazuli" to R.drawable.lapis_lazuli,
                    "lepidolite" to R.drawable.lepidolite,
                    "lion_skin" to R.drawable.lion_skin,
                    "malachite" to R.drawable.malachite,
                    "milky_quartz" to R.drawable.milky_quartz,
                    "mookaite" to R.drawable.mookaite,
                    "moonstone" to R.drawable.moon_stone,
                    "obsidian" to R.drawable.obsidian,
                    "onyx" to R.drawable.onyx,
                    "opal" to R.drawable.opal,
                    "orange_aventurine" to R.drawable.orange_aventurine,
                    "orange_calcite" to R.drawable.orange_calcite,
                    "pearl" to R.drawable.pearl,
                    "peridot" to R.drawable.peridot,
                    "petrified_wood" to R.drawable.petrified_wood,
                    "pink_quartz" to R.drawable.pink_quartz,
                    "pink_tourmaline" to R.drawable.pink_tourmaline,
                    "prehnite" to R.drawable.prehnite,
                    "purple_aventurine" to R.drawable.purple_aventurine,
                    "purple_fluorite" to R.drawable.purple_fluorite,
                    "purple_sapphire" to R.drawable.purple_sapphire,
                    "pyrite" to R.drawable.pyrite,
                    "quartz_crystal" to R.drawable.quartz_crystal,
                    "quartz_tourmaline" to R.drawable.quartz_tourmaline,
                    "red_agate" to R.drawable.red_agate,
                    "red_aventurine" to R.drawable.red_aventurine,
                    "red_garnet" to R.drawable.red_garnet,
                    "red_jasper" to R.drawable.red_jasper,
                    "rhodonite" to R.drawable.rhodonite,
                    "rhodochrosite" to R.drawable.rhodochrosite,
                    "rose_opal" to R.drawable.rose_opal,
                    "rose_quartz" to R.drawable.rose_quartz,
                    "ruby" to R.drawable.ruby,
                    "selenite" to R.drawable.selenite,
                    "serpentine" to R.drawable.serpentine,
                    "shiva_lingam" to R.drawable.shiva_lingam,
                    "shungite" to R.drawable.shungite,
                    "smoky_quartz" to R.drawable.smoky_quartz,
                    "sodalite" to R.drawable.sodalite,
                    "sugilite" to R.drawable.sugilite,
                    "sunstone" to R.drawable.sunstone,
                    "tanzanite" to R.drawable.tanzanite,
                    "tiger_eye" to R.drawable.tiger_eye,
                    "topaz" to R.drawable.topaz,
                    "turquoise" to R.drawable.turquoise,
                    "unakite" to R.drawable.unakite,
                    "volcanic_stone" to R.drawable.volcanic_stone,
                    "watermelon_tourmaline" to R.drawable.watermelon_tourmaline,
                    "white_topaz" to R.drawable.white_topaz,
                    "yellow_fluorite" to R.drawable.yellow_fluorite,
                    "yellow_jade" to R.drawable.yellow_jade,
                    "yellow_jasper" to R.drawable.yellow_jasper,
                    "yellow_quartz" to R.drawable.yellow_quartz,
                    "yellow_tourmaline" to R.drawable.yellow_tourmaline
                )

                stoneList.forEach { item ->
                    val iconResId = drawableMap[item.imageName] ?: R.drawable.agate
                    val resourceUri = "android.resource://${context.packageName}/$iconResId"

                    val stone = Stone(name = item.imageName, imageUri = resourceUri)
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

                val drawableMap = mapOf(
                    "root_chakra" to R.drawable.root_chakra,
                    "sacral_chakra" to R.drawable.sacral_chakra,
                    "solar_plexus_chakra" to R.drawable.solar_plexus_chakra,
                    "heart_chakra" to R.drawable.heart_chakra,
                    "throat_chakra" to R.drawable.throat_chakra,
                    "third_eye_chakra" to R.drawable.third_eye_chakra,
                    "crown_chakra" to R.drawable.crown_chakra
                )

                list.forEach { item ->
                    val icon = drawableMap[item.imageBase] ?: R.drawable.root_chakra
                    val chakra = Chakra(sanskritName = item.key, iconResId = icon)

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

