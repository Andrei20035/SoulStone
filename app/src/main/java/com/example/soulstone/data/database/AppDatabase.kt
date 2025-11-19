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
                    stoneDao = database.stoneDao()
                )
            }
        }

        private suspend fun populateDatabase(
            zodiacSignDao: ZodiacSignDao,
            benefitDao: BenefitDao,
            chineseDao: ChineseZodiacSignDao,
            stoneDao: StoneDao

        ) {
            Log.d("AppDatabase", "Populating database with lists...")

            // --- 1. Populate Zodiac Signs ---
            val zodiacData = listOf(
                ZodiacInput("Aries", "Mar 21", "Apr 19", R.drawable.aries, "Fire", "Mars", "Aries is the first astrological sign, known for its fiery energy and leadership."),
                ZodiacInput("Taurus", "Apr 20", "May 20", R.drawable.taurus, "Earth", "Venus", "Taurus is the second astrological sign, known for its grounded, practical, and reliable nature."),
                ZodiacInput("Gemini", "May 21", "Jun 20", R.drawable.gemini, "Air", "Mercury", "Gemini is the third astrological sign, known for its curious, adaptable, and communicative spirit."),
                ZodiacInput("Cancer", "Jun 21", "Jul 22", R.drawable.cancer, "Water", "Moon", "Cancer is the fourth astrological sign, known for its intuitive, nurturing, and protective qualities."),
                ZodiacInput("Leo", "Jul 23", "Aug 22", R.drawable.leo, "Fire", "Sun", "Leo is the fifth astrological sign, known for its confident, generous, and charismatic presence."),
                ZodiacInput("Virgo", "Aug 23", "Sep 22", R.drawable.virgo, "Earth", "Mercury", "Virgo is the sixth astrological sign, known for its meticulous, analytical, and helpful nature."),
                ZodiacInput("Libra", "Sep 23", "Oct 22", R.drawable.libra, "Air", "Venus", "Libra is the seventh astrological sign, known for its diplomatic, fair-minded, and social grace."),
                ZodiacInput("Scorpio", "Oct 23", "Nov 21", R.drawable.scorpio, "Water", "Pluto", "Scorpio is the eighth astrological sign, known for its passionate, resourceful, and mysterious aura."),
                ZodiacInput("Sagittarius", "Nov 22", "Dec 21", R.drawable.sagittarius, "Fire", "Jupiter", "Sagittarius is the ninth astrological sign, known for its optimistic, adventurous, and philosophical outlook."),
                ZodiacInput("Capricorn", "Dec 22", "Jan 19", R.drawable.capricorn, "Earth", "Saturn", "Capricorn is the tenth astrological sign, known for its disciplined, responsible, and ambitious drive."),
                ZodiacInput("Aquarius", "Jan 20", "Feb 18", R.drawable.aquarius, "Air", "Uranus", "Aquarius is the eleventh astrological sign, known for its independent, humanitarian, and original ideas."),
                ZodiacInput("Pisces", "Feb 19", "Mar 20", R.drawable.pisces, "Water", "Neptune", "Pisces is the twelfth astrological sign, known for its compassionate, artistic, and intuitive nature.")
            )

            zodiacData.forEach { item ->
                val sign = ZodiacSign(name = item.name, startDate = item.start, endDate = item.end, iconResId = item.icon)
                val translation = ZodiacSignTranslation(
                    zodiacSignId = 0,
                    languageCode = LanguageCode.ENGLISH,
                    name = item.name,
                    description = item.desc,
                    element = item.element,
                    rulingPlanet = item.planet
                )
                zodiacSignDao.insertZodiacSignWithTranslations(sign, listOf(translation))
            }

            // --- 2. Populate Benefits ---
            val benefitData = listOf(
                BenefitInput("vitality", "Vitality", R.drawable.stone_1),
                BenefitInput("mindfulness", "Mindfulness", R.drawable.stone_2),
                BenefitInput("personal_growth", "Personal Growth", R.drawable.stone_3),
                BenefitInput("anxiety_and_depression", "Anxiety & Depression", R.drawable.stone_4),
                BenefitInput("love_relationships", "Love Relationships", R.drawable.stone_5),
                BenefitInput("anger_and_stress_relief", "Anger & Stress Relief", R.drawable.stone_6),
                BenefitInput("transformation", "Transformation", R.drawable.stone_7),
                BenefitInput("clarity_and_focus", "Clarity & Focus", R.drawable.stone_8),
                BenefitInput("career_and_money", "Career & Money", R.drawable.stone_4_r),
                BenefitInput("protection", "Protection", R.drawable.stone_3_r),
                BenefitInput("self_control", "Self-Control", R.drawable.stone_2_r),
                BenefitInput("communication", "Communication", R.drawable.stone_1_r),
                BenefitInput("physical_health", "Physical Health", R.drawable.stone_8_r),
                BenefitInput("intuition", "Intuition", R.drawable.stone_7_r),
                BenefitInput("enhanced_focus", "Enhanced Focus", R.drawable.stone_6_r),
                BenefitInput("spirituality", "Spirituality", R.drawable.stone_5_r),
                BenefitInput("mental_health", "Mental Health", R.drawable.stone_5),
                BenefitInput("creativity", "Creativity", R.drawable.stone_6),
                BenefitInput("good_fortune_and_luck", "Good Fortune & Luck", R.drawable.stone_7),
                BenefitInput("manifestation", "Manifestation", R.drawable.stone_8),
                BenefitInput("emotional_balance", "Emotional Balance", R.drawable.stone_4_r)
            )

            benefitData.forEach { item ->
                val benefit = Benefit(name = item.key, imageResId = item.icon)
                val translation = BenefitTranslation(
                    benefitId = 0,
                    languageCode = LanguageCode.ENGLISH,
                    name = item.name
                )
                benefitDao.insertBenefitWithTranslations(benefit, listOf(translation))
            }

            // --- 3. Populate Chinese Zodiac Signs ---
            val chineseZodiacData = listOf(
                ChineseInput("Rat", R.drawable.rat, R.drawable.rat_border, R.drawable.rat_color, "1960, 1972, 1984, 1996, 2008, 2020", "The Rat is the first sign, known for being quick-witted, resourceful, and charming."),
                ChineseInput("Ox", R.drawable.ox, R.drawable.ox_border, R.drawable.ox_color, "1961, 1973, 1985, 1997, 2009, 2021", "The Ox is the second sign, valued for its diligence, dependability, and determination."),
                ChineseInput("Tiger", R.drawable.tiger, R.drawable.tiger_border, R.drawable.tiger_color, "1962, 1974, 1986, 1998, 2010, 2022", "The Tiger is the third sign, known for its bravery, confidence, and competitiveness."),
                ChineseInput("Rabbit", R.drawable.rabbit, R.drawable.rabbit_border, R.drawable.rabbit_color, "1963, 1975, 1987, 1999, 2011, 2023", "The Rabbit is the fourth sign, representing elegance, kindness, and vigilance."),
                ChineseInput("Dragon", R.drawable.dragon, R.drawable.dragon_border, R.drawable.dragon_color, "1964, 1976, 1988, 2000, 2012, 2024", "The Dragon is the fifth sign, a symbol of power, charisma, and ambition."),
                ChineseInput("Snake", R.drawable.snake, R.drawable.snake_border, R.drawable.snake_color, "1965, 1977, 1989, 2001, 2013, 2025", "The Snake is the sixth sign, known for its intelligence, intuition, and mysterious nature."),
                ChineseInput("Horse", R.drawable.horse, R.drawable.horse_border, R.drawable.horse_color, "1966, 1978, 1990, 2002, 2014, 2026", "The Horse is the seventh sign, representing energy, independence, and a free spirit."),
                ChineseInput("Goat", R.drawable.goat, R.drawable.goat_border, R.drawable.goat_color, "1967, 1979, 1991, 2003, 2015, 2027", "The Goat is the eighth sign, known for its gentle, compassionate, and artistic nature."),
                ChineseInput("Monkey", R.drawable.monkey, R.drawable.monkey_border, R.drawable.monkey_color, "1968, 1980, 1992, 2004, 2016, 2028", "The Monkey is the ninth sign, characterized by its wit, curiosity, and playful personality."),
                ChineseInput("Rooster", R.drawable.rooster, R.drawable.rooster_border, R.drawable.rooster_color, "1969, 1981, 1993, 2005, 2017, 2029", "The Rooster is the tenth sign, known for being observant, hardworking, and courageous."),
                ChineseInput("Dog", R.drawable.dog, R.drawable.dog_border, R.drawable.dog_color, "1970, 1982, 1994, 2006, 2018, 2030", "The Dog is the eleventh sign, valued for its loyalty, honesty, and strong sense of justice."),
                ChineseInput("Pig", R.drawable.pig, R.drawable.pig_border, R.drawable.pig_color, "1971, 1983, 1995, 2007, 2019, 2031", "The Pig is the twelfth sign, known for its compassion, generosity, and diligence.")
            )

            chineseZodiacData.forEach { item ->
                val sign = ChineseZodiacSign(
                    name = item.name,
                    iconResId = item.icon,
                    iconResIdBorder = item.iconBorder,
                    iconResIdColor = item.iconColor,
                    recentYears = item.years
                )
                val translation = ChineseZodiacSignTranslation(
                    chineseSignId = 0,
                    languageCode = LanguageCode.ENGLISH,
                    name = item.name,
                    description = item.desc
                )
                chineseDao.insertChineseSignWithTranslations(sign, listOf(translation))
            }

            val stoneData = listOf(
                StoneInput("Agate", R.drawable.agate),
                StoneInput("Amazonite", R.drawable.amazonite),
                StoneInput("Amber", R.drawable.amber),
                StoneInput("Amethyst", R.drawable.amethyst),
                StoneInput("Angelite", R.drawable.angelite),
                StoneInput("Apatite", R.drawable.apatite),
                StoneInput("Aquamarine", R.drawable.aquamarine),
                StoneInput("Aventurine", R.drawable.aventurine),
                StoneInput("Black Obsidian", R.drawable.black_obsidian),
                StoneInput("Black Tourmaline", R.drawable.black_tourmaline),
                StoneInput("Bloodstone", R.drawable.bloodstone),
                StoneInput("Blue Agate", R.drawable.blue_agate),
                StoneInput("Blue Calcite", R.drawable.blue_calcite),
                StoneInput("Blue Chalcedony", R.drawable.blue_chalcedony),
                StoneInput("Blue Kyanite", R.drawable.blue_kyanite),
                StoneInput("Bronzite", R.drawable.bronzite),
                StoneInput("Carnelian", R.drawable.carnelian),
                StoneInput("Chrysocolla", R.drawable.chrysocolla),
                StoneInput("Chrysoprase", R.drawable.chrysoprase),
                StoneInput("Citrine", R.drawable.citrine),
                StoneInput("Clear Quartz", R.drawable.clear_quartz),
                StoneInput("Coral", R.drawable.coral),
                StoneInput("Dalmatian Jasper", R.drawable.dalmatian_jasper),
                StoneInput("Diamond", R.drawable.diamond),
                StoneInput("Emerald", R.drawable.emerald),
                StoneInput("Fluorite", R.drawable.fluorite),
                StoneInput("Golden Chalcedony", R.drawable.golden_chalcedony),
                StoneInput("Green Aventurine", R.drawable.green_aventurine),
                StoneInput("Green Jasper", R.drawable.green_jasper),
                StoneInput("Green Quartz", R.drawable.green_quartz),
                StoneInput("Hawk Eye", R.drawable.hawk_eye_stone),
                StoneInput("Hematite", R.drawable.hematite),
                StoneInput("Howlite", R.drawable.howlite),
                StoneInput("Jade", R.drawable.jade),
                StoneInput("Jasper", R.drawable.jasper),
                StoneInput("Labradorite", R.drawable.labradorite),
                StoneInput("Lapis Lazuli", R.drawable.lapis_lazuli),
                StoneInput("Lepidolite", R.drawable.lepidolite),
                StoneInput("Lion Skin", R.drawable.lion_skin),
                StoneInput("Malachite", R.drawable.malachite),
                StoneInput("Milky Quartz", R.drawable.milky_quartz),
                StoneInput("Mookaite", R.drawable.mookaite),
                StoneInput("Moonstone", R.drawable.moon_stone),
                StoneInput("Obsidian", R.drawable.obsidian),
                StoneInput("Onyx", R.drawable.onyx),
                StoneInput("Opal", R.drawable.opal),
                StoneInput("Orange Aventurine", R.drawable.orange_aventurine),
                StoneInput("Orange Calcite", R.drawable.orange_calcite),
                StoneInput("Pearl", R.drawable.pearl),
                StoneInput("Peridot", R.drawable.peridot),
                StoneInput("Petrified Wood", R.drawable.petrified_wood),
                StoneInput("Pink Quartz", R.drawable.pink_quartz),
                StoneInput("Pink Tourmaline", R.drawable.pink_tourmaline),
                StoneInput("Prehnite", R.drawable.prehnite),
                StoneInput("Purple Aventurine", R.drawable.purple_aventurine),
                StoneInput("Purple Fluorite", R.drawable.purple_fluorite),
                StoneInput("Purple Sapphire", R.drawable.purple_sapphire),
                StoneInput("Pyrite", R.drawable.pyrite),
                StoneInput("Quartz Crystal", R.drawable.quartz_crystal),
                StoneInput("Quartz Tourmaline", R.drawable.quartz_tourmaline),
                StoneInput("Red Agate", R.drawable.red_agate),
                StoneInput("Red Aventurine", R.drawable.red_aventurine),
                StoneInput("Red Garnet", R.drawable.red_garnet),
                StoneInput("Red Jasper", R.drawable.red_jasper),
                StoneInput("Rhodonite", R.drawable.rhodonite),
                StoneInput("Rhodochrosite", R.drawable.rodocrosite),
                StoneInput("Rose Opal", R.drawable.rose_opal),
                StoneInput("Rose Quartz", R.drawable.rose_quartz),
                StoneInput("Ruby", R.drawable.ruby),
                StoneInput("Selenite", R.drawable.selenite),
                StoneInput("Serpentine", R.drawable.serpentine_stone),
                StoneInput("Shiva Lingam", R.drawable.shiva_lingam),
                StoneInput("Shungite", R.drawable.shungite),
                StoneInput("Smoky Quartz", R.drawable.smoky_quartz),
                StoneInput("Sodalite", R.drawable.sodalite),
                StoneInput("Sugilite", R.drawable.sugilite),
                StoneInput("Sunstone", R.drawable.sunstone),
                StoneInput("Tanzanite", R.drawable.tanzanite),
                StoneInput("Tiger Eye", R.drawable.tiger_eye),
                StoneInput("Topaz", R.drawable.topaz),
                StoneInput("Turquoise", R.drawable.turquoise),
                StoneInput("Unakite", R.drawable.unakite),
                StoneInput("Volcanic Stone", R.drawable.volcanic_stone),
                StoneInput("Watermelon Tourmaline", R.drawable.watermelon_tourmaline),
                StoneInput("White Topaz", R.drawable.white_topaz),
                StoneInput("Yellow Fluorite", R.drawable.yellow_fluorite),
                StoneInput("Yellow Jade", R.drawable.yellow_jade),
                StoneInput("Yellow Jasper", R.drawable.yellow_jasper),
                StoneInput("Yellow Quartz", R.drawable.yellow_quartz),
                StoneInput("Yellow Tourmaline", R.drawable.yellow_tourmaline)
            )

            stoneData.forEach { item ->
                // Create the Base Stone
                // Note: Use item.name.lowercase().replace(" ", "_") if you want the key to be snake_case
                // Or just use item.name if you don't mind keys having spaces.
                val stoneKey = item.name.lowercase().replace(" ", "_")
                val resourceUri = "android.resource://${context.packageName}/${item.image}"

                val stone = Stone(name = stoneKey, imageUri = resourceUri)

                // Create the Translation
                val translation = StoneTranslation(
                    stoneId = 0, // Room Auto-generate
                    languageCode = LanguageCode.ENGLISH,
                    name = item.name,
                    description = "Description for ${item.name}..." // You can update descriptions later
                )

                // Insert
                stoneDao.insertTranslation()
            }

            Log.d("AppDatabase", "Finished populating database.")
        }
    }

    // --- Data Holder Classes (Used only for population) ---
    private data class ZodiacInput(val name: String, val start: String, val end: String, val icon: Int, val element: String, val planet: String, val desc: String)
    private data class BenefitInput(val key: String, val name: String, val icon: Int)
    private data class ChineseInput(val name: String, val icon: Int, val iconBorder: Int, val iconColor: Int, val years: String, val desc: String)
    private data class StoneInput(val name: String, val image: Int)
}