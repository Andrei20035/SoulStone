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
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.util.LanguageCode
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
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
            CoroutineScope(Dispatchers.IO).launch {
                val database = dbProvider.get()
                populateDatabase(
                    zodiacSignDao = database.zodiacSignDao(),
                    benefitDao = database.benefitDao(),
                    chineseDao = database.chineseZodiacSignDao(),
                    stoneDao = database.stoneDao(),
                    chakraDao = database.chakraDao()
                )
            }
        }

        private suspend fun populateDatabase(
            zodiacSignDao: ZodiacSignDao,
            benefitDao: BenefitDao,
            chineseDao: ChineseZodiacSignDao,
            stoneDao: StoneDao,
            chakraDao: ChakraDao

        ) {
            Log.d("AppDatabase", "Populating database with lists...")

            // --- 1. Populate Zodiac Signs from JSON ---
            try {
                val jsonString = context.assets.open("initial_zodiac_signs.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<ZodiacJsonItem>>() {}.type
                val zodiacList: List<ZodiacJsonItem> = Gson().fromJson(jsonString, listType)

                val zodiacDrawableMap = mapOf(
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
                    val finalIcon = zodiacDrawableMap[item.imageName] ?: R.drawable.aries

                    val sign = ZodiacSign(
                        name = item.key,
                        startDate = item.startDate,
                        endDate = item.endDate,
                        iconResId = finalIcon
                    )

                    val translations = item.translations.map { (langKey, data) ->
                        val code = mapLanguageCode(langKey)

                        ZodiacSignTranslation(
                            zodiacSignId = 0,
                            languageCode = code,
                            name = data.name,
                            description = data.description,
                            element = data.element,
                            rulingPlanet = data.planet
                        )
                    }

                    zodiacSignDao.insertZodiacSignWithTranslations(sign, translations)
                }
                Log.d("AppDatabase", "Zodiac signs populated successfully.")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Zodiac JSON", e)
            }

            // --- 2. Populate Benefits from JSON ---
            try {
                val jsonString = context.assets.open("initial_benefits.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<BenefitJsonItem>>() {}.type
                val benefitList: List<BenefitJsonItem> = Gson().fromJson(jsonString, listType)

                // Manual map for safety and R8 optimization
                val benefitDrawableMap = mapOf(
                    "stone_1" to R.drawable.stone_1,
                    "stone_2" to R.drawable.stone_2,
                    "stone_3" to R.drawable.stone_3,
                    "stone_4" to R.drawable.stone_4,
                    "stone_5" to R.drawable.stone_5,
                    "stone_6" to R.drawable.stone_6,
                    "stone_7" to R.drawable.stone_7,
                    "stone_8" to R.drawable.stone_8,
                    // Rotated versions
                    "stone_1_r" to R.drawable.stone_1_r,
                    "stone_2_r" to R.drawable.stone_2_r,
                    "stone_3_r" to R.drawable.stone_3_r,
                    "stone_4_r" to R.drawable.stone_4_r,
                    "stone_5_r" to R.drawable.stone_5_r,
                    "stone_6_r" to R.drawable.stone_6_r,
                    "stone_7_r" to R.drawable.stone_7_r,
                    "stone_8_r" to R.drawable.stone_8_r
                )

                benefitList.forEach { item ->
                    // Lookup image, fallback to stone_1 if missing
                    val iconResId = benefitDrawableMap[item.imageName] ?: R.drawable.stone_1

                    val benefit = Benefit(
                        name = item.key,
                        imageResId = iconResId
                    )

                    val translations = item.translations.map { (langKey, data) ->
                        val code = mapLanguageCode(langKey)

                        BenefitTranslation(
                            benefitId = 0,
                            languageCode = code,
                            name = data.name
                        )
                    }

                    benefitDao.insertBenefitWithTranslations(benefit, translations)
                }
                Log.d("AppDatabase", "Benefits populated successfully.")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Benefits JSON", e)
            }

            // --- 3. Populate Chinese Zodiac Signs ---
            try {
                val jsonString = context.assets.open("initial_chinese_zodiac.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<ChineseZodiacJsonItem>>() {}.type
                val chineseList: List<ChineseZodiacJsonItem> = Gson().fromJson(jsonString, listType)

                // Manual Map to hold the triplet of images for each sign
                val chineseDrawableMap = mapOf(
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

                chineseList.forEach { item ->
                    val icons = chineseDrawableMap[item.imageBase]
                        ?: ChineseIcons(R.drawable.rat, R.drawable.rat_border, R.drawable.rat_color)

                    val sign = ChineseZodiacSign(
                        name = item.key,
                        iconResId = icons.icon,
                        iconResIdBorder = icons.border,
                        iconResIdColor = icons.color,
                        recentYears = item.years
                    )

                    var parentId = chineseDao.insertChineseSign(sign)

                    if (parentId == -1L) {
                        // We use the name (e.g., "Rat") to look it up
                        val existingId = chineseDao.getChineseSignIdByKey(item.key)

                        // Handle the edge case where something went wrong and it's still null
                        if (existingId != null) {
                            parentId = existingId.toLong()
                        } else {
                            Log.e("DB", "Error: Sign ${item.key} exists but ID not found.")
                            return@forEach // Skip this iteration
                        }
                    }

                    val translations = item.translations.map { (langKey, data) ->
                        val code = mapLanguageCode(langKey)
                        ChineseZodiacSignTranslation(
                            chineseSignId = parentId.toInt(), // Auto-generated by Room
                            languageCode = code,
                            name = data.name,
                            description = data.description,
                            traits = data.traits,
                            bestMatch = data.bestMatch,
                            worstMatch = data.worstMatch,
                            compatibilityDescription = data.compatibilityDesc,
                            gemstoneDescription = data.gemstoneDesc
                        )
                    }

                    chineseDao.insertChineseTranslations(translations)
                }
                Log.d("AppDatabase", "Chinese Zodiac signs populated successfully.")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Chinese Zodiac JSON", e)
            }

            try {
                val jsonString = context.assets.open("initial_stones.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<StoneJsonItem>>() {}.type
                val stoneList: List<StoneJsonItem> = Gson().fromJson(jsonString, listType)

                // Huge map to link JSON string keys to Drawable IDs safely
                val stoneDrawableMap = mapOf(
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
                    "hawk_eye" to R.drawable.hawk_eye_stone,
                    "hematite" to R.drawable.hematite,
                    "howlite" to R.drawable.howlite,
                    "jade" to R.drawable.jade,
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
                    "rhodochrosite" to R.drawable.rodocrosite,
                    "rose_opal" to R.drawable.rose_opal,
                    "rose_quartz" to R.drawable.rose_quartz,
                    "ruby" to R.drawable.ruby,
                    "selenite" to R.drawable.selenite,
                    "serpentine" to R.drawable.serpentine_stone,
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
                    // 1. Find the Drawable ID (Default to Agate if not found)
                    val iconResId = stoneDrawableMap[item.imageName] ?: R.drawable.agate

                    // 2. Construct the URI String required by your Stone Entity
                    val resourceUri = "android.resource://${context.packageName}/$iconResId"

                    // 3. Create Base Stone Entity
                    val stone = Stone(
                        name = item.imageName, // This serves as the snake_case key
                        imageUri = resourceUri
                    )

                    // 4. Create Translations
                    val translations = item.translations.map { (langKey, data) ->
                        val code = mapLanguageCode(langKey)

                        StoneTranslation(
                            stoneId = 0, // Auto-generated
                            languageCode = code,
                            name = data.name,
                            description = data.description
                        )
                    }

                    // 5. Insert (Assuming you have this method in your DAO)
                    // Note: If you don't have a batch insert for translations, iterate and insert one by one
                    stoneDao.insertStoneWithTranslations(stone, translations)
                }
                Log.d("AppDatabase", "Stones populated successfully.")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Stone JSON", e)
            }

            try {
                Log.d("AppDatabase", "Linking Chinese Zodiacs to Stones...")

                // 1. Read the JSON file
                val jsonString = context.assets.open("initial_chinese_associations.json")
                    .bufferedReader()
                    .use { it.readText() }

                // 2. Parse into List
                val listType = object : TypeToken<List<ChineseAssociationJsonItem>>() {}.type
                val associationList: List<ChineseAssociationJsonItem> = Gson().fromJson(jsonString, listType)

                // 3. Iterate through every Zodiac entry in the JSON
                associationList.forEach { item ->

                    // A. Find the Zodiac ID (e.g., Look up "rat" -> Get ID 1)
                    val zodiacId = chineseDao.getChineseSignIdByKey(item.zodiacKey)

                    if (zodiacId != null) {
                        // B. Iterate through the stones listed for this Zodiac
                        item.stoneKeys.forEach { stoneNameKey ->

                            // C. Find the Stone ID (e.g., Look up "garnet" -> Get ID 12)
                            val stoneId = stoneDao.getStoneIdByKey(stoneNameKey)

                            if (stoneId != null) {
                                // D. Create the Link
                                val crossRef = StoneChineseZodiacCrossRef(
                                    stoneId = stoneId,
                                    chineseZodiacSignId = zodiacId
                                )

                                // E. Insert into DB
                                stoneDao.insertChineseZodiacCrossRef(crossRef)
                            } else {
                                // Log warning if stone name in JSON doesn't match any stone in DB
                                Log.w("AppDatabase", "Skip Link: Stone '$stoneNameKey' not found in DB.")
                            }
                        }
                    } else {
                        Log.w("AppDatabase", "Skip Link: Zodiac '${item.zodiacKey}' not found in DB.")
                    }
                }
                Log.d("AppDatabase", "Chinese Zodiac associations finished.")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Association JSON", e)
            }

            // --- 4. Populate Chakras ---
            try {
                // 1. Load the JSON file
                val jsonString = context.assets.open("initial_chakras.json") // Make sure this file exists in assets
                    .bufferedReader()
                    .use { it.readText() }

                // 2. Parse JSON using Gson
                val listType = object : TypeToken<List<ChakraJsonItem>>() {}.type
                val chakraList: List<ChakraJsonItem> = Gson().fromJson(jsonString, listType)

                // 3. Map JSON string keys to Android Drawable Resources
                // Ensure these drawables exist in your res/drawable folder
                val chakraDrawableMap = mapOf(
                    "root_chakra" to R.drawable.root_chakra,
                    "sacral_chakra" to R.drawable.sacral_chakra,
                    "solar_plexus_chakra" to R.drawable.solar_plexus_chakra,
                    "heart_chakra" to R.drawable.heart_chakra,
                    "throat_chakra" to R.drawable.throat_chakra,
                    "third_eye_chakra" to R.drawable.third_eye_chakra,
                    "crown_chakra" to R.drawable.crown_chakra
                )

                // 4. Loop and Insert
                chakraList.forEach { item ->
                    // A. Get the Icon
                    val iconRes = chakraDrawableMap[item.imageBase]
                        ?: R.drawable.root_chakra // Fallback icon to prevent crashes

                    // B. Create the Parent Entity
                    val chakra = Chakra(
                        sanskritName = item.key,
                        iconResId = iconRes
                    )

                    // C. Insert Parent (Handling IGNORE strategy)
                    var parentId = chakraDao.insertChakra(chakra)

                    if (parentId == -1L) {
                        // The Chakra already exists, fetch its real ID using the unique Sanskrit name
                        val existingId = chakraDao.getChakraIdByName(item.key)

                        if (existingId != null) {
                            parentId = existingId.toLong()
                        } else {
                            Log.e("DB", "Error: Chakra ${item.key} exists but ID not found.")
                            return@forEach // Skip this iteration
                        }
                    }

                    // D. Map JSON Translations to Entity Translations
                    val translations = item.translations.map { (langKey, data) ->
                        val code = mapLanguageCode(langKey) // Use your existing helper function

                        ChakraTranslation(
                            chakraId = parentId.toInt(), // Link to the Parent ID retrieved above
                            languageCode = code,
                            name = data.name,
                            description = data.description,
                            location = data.location,

                            // Safe mapping for new fields (defaults to empty string if missing in JSON)
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

                    // E. Insert Translations
                    chakraDao.insertChakraTranslations(translations)
                }
                Log.d("AppDatabase", "Chakras populated successfully.")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error parsing Chakra JSON", e)
            }

            Log.d("AppDatabase", "Finished populating database.")
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
    data class ZodiacJsonItem(
        @SerializedName("key") val key: String,
        @SerializedName("start_date") val startDate: String,
        @SerializedName("end_date") val endDate: String,
        @SerializedName("image_name") val imageName: String,
        @SerializedName("translations") val translations: Map<String, ZodiacJsonTranslation>
    )

    data class ZodiacJsonTranslation(
        @SerializedName("name") val name: String,
        @SerializedName("element") val element: String,
        @SerializedName("planet") val planet: String,
        @SerializedName("desc") val description: String
    )



    data class BenefitJsonItem(
        @SerializedName("key") val key: String,
        @SerializedName("image_name") val imageName: String,
        @SerializedName("translations") val translations: Map<String, BenefitJsonTranslation>
    )

    data class BenefitJsonTranslation(
        @SerializedName("name") val name: String
    )



    data class ChineseZodiacJsonItem(
        @SerializedName("key") val key: String,
        @SerializedName("image_base") val imageBase: String,
        @SerializedName("years") val years: String,
        @SerializedName("translations") val translations: Map<String, ChineseZodiacTranslationData>
    )

    data class ChineseZodiacTranslationData(
        @SerializedName("name") val name: String,
        @SerializedName("desc") val description: String,
        @SerializedName("traits") val traits: String,
        @SerializedName("best_match") val bestMatch: String,
        @SerializedName("worst_match") val worstMatch: String,
        @SerializedName("compatibility_desc") val compatibilityDesc: String,
        @SerializedName("gemstone_desc") val gemstoneDesc: String
    )

    // Helper class to hold the triplet of images safely
    data class ChineseIcons(
        val icon: Int,
        val border: Int,
        val color: Int
    )



    data class StoneJsonItem(
        // Matches "image_name": "yellow_jasper" in your JSON
        @SerializedName("image_name") val imageName: String,
        @SerializedName("translations") val translations: Map<String, StoneJsonTranslationData>
    )

    data class StoneJsonTranslationData(
        @SerializedName("name") val name: String,
        @SerializedName("desc") val description: String
    )



    data class ChineseAssociationJsonItem(
        @SerializedName("zodiac_key") val zodiacKey: String,
        @SerializedName("stones") val stoneKeys: List<String>
    )






    data class ChakraJsonItem(
        val key: String,
        @SerializedName("image_base") val imageBase: String,
        val translations: Map<String, ChakraTranslationJsonData>
    )

    data class ChakraTranslationJsonData(
        val name: String,
        @SerializedName("desc") val description: String,
        val location: String,

        // --- NEW FIELDS ---
        @SerializedName("ruling_planet") val rulingPlanet: String,
        val element: String,

        @SerializedName("stone_colors") val stoneColors: String,
        @SerializedName("healing_qualities") val healingQualities: String,
        val stones: String,
        @SerializedName("body_placement") val bodyPlacement: String,
        @SerializedName("house_placement") val housePlacement: String,
        val herbs: String,
        @SerializedName("essential_oils") val essentialOils: String
    )
}