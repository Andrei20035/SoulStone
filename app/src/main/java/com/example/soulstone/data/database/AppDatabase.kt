package com.example.soulstone.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
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
import com.example.soulstone.domain.model.chineseZodiacSigns
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

        // 2. Override onCreate
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "onCreate called — populating database")
            // 3. Launch a coroutine
            CoroutineScope(Dispatchers.IO).launch {
                // 4. Get the DAOs by calling getDatabase()
                // This is safe now. The coroutine runs *after* the
                // build() method has returned and INSTANCE is set.
                // The second call to getDatabase() will just return the
                // existing INSTANCE.
                val database = dbProvider.get()
                populateDatabase(
                    zodiacSignDao = database.zodiacSignDao(),
                    benefitDao = database.benefitDao(),
                    chineseDao = database.chineseZodiacSignDao()
                )
            }
        }

        /**
         * Populates the database with the 12 Zodiac signs and their base English translations.
         * This function is called from the RoomDatabase.Callback's onCreate method.
         */
        suspend fun populateDatabase(
            zodiacSignDao: ZodiacSignDao,
            benefitDao: BenefitDao,
            chineseDao: ChineseZodiacSignDao
        ) {
            Log.d("AppDatabase", "Populating database...")
            // Note: We do NOT need database.withTransaction {} here.
            // Your @Transaction annotation on the DAO method handles it automatically.
            // Also, I've fixed the code from your prompt to make only one call per sign.

            // 1. Aries
            val aries = ZodiacSign(
                name = "Aries",
                startDate = "Mar 21",
                endDate = "Apr 19",
                iconResId = R.drawable.aries // <-- IMPORTANT: Change to your drawable ID
            )
            val ariesEn = ZodiacSignTranslation(
                zodiacSignId = 0, // Will be set by DAO
                languageCode = LanguageCode.ENGLISH,
                name = "Aries",
                description = "Aries is the first astrological sign, known for its fiery energy and leadership.",
                element = "Fire",
                rulingPlanet = "Mars"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(aries, listOf(ariesEn))

            // 2. Taurus
            val taurus = ZodiacSign(
                name = "Taurus",
                startDate = "Apr 20",
                endDate = "May 20",
                iconResId = R.drawable.taurus // <-- IMPORTANT: Change to your drawable ID
            )
            val taurusEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Taurus",
                description = "Taurus is the second astrological sign, known for its grounded, practical, and reliable nature.",
                element = "Earth",
                rulingPlanet = "Venus"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(taurus, listOf(taurusEn))

            // 3. Gemini
            val gemini = ZodiacSign(
                name = "Gemini",
                startDate = "May 21",
                endDate = "Jun 20",
                iconResId = R.drawable.gemini // <-- IMPORTANT: Change to your drawable ID
            )
            val geminiEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Gemini",
                description = "Gemini is the third astrological sign, known for its curious, adaptable, and communicative spirit.",
                element = "Air",
                rulingPlanet = "Mercury"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(gemini, listOf(geminiEn))

            // 4. Cancer
            val cancer = ZodiacSign(
                name = "Cancer",
                startDate = "Jun 21",
                endDate = "Jul 22",
                iconResId = R.drawable.cancer // <-- IMPORTANT: Change to your drawable ID
            )
            val cancerEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Cancer",
                description = "Cancer is the fourth astrological sign, known for its intuitive, nurturing, and protective qualities.",
                element = "Water",
                rulingPlanet = "Moon"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(cancer, listOf(cancerEn))

            // 5. Leo
            val leo = ZodiacSign(
                name = "Leo",
                startDate = "Jul 23",
                endDate = "Aug 22",
                iconResId = R.drawable.leo // <-- IMPORTANT: Change to your drawable ID
            )
            val leoEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Leo",
                description = "Leo is the fifth astrological sign, known for its confident, generous, and charismatic presence.",
                element = "Fire",
                rulingPlanet = "Sun"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(leo, listOf(leoEn))

            // 6. Virgo
            val virgo = ZodiacSign(
                name = "Virgo",
                startDate = "Aug 23",
                endDate = "Sep 22",
                iconResId = R.drawable.virgo // <-- IMPORTANT: Change to your drawable ID
            )
            val virgoEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Virgo",
                description = "Virgo is the sixth astrological sign, known for its meticulous, analytical, and helpful nature.",
                element = "Earth",
                rulingPlanet = "Mercury"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(virgo, listOf(virgoEn))

            // 7. Libra
            val libra = ZodiacSign(
                name = "Libra",
                startDate = "Sep 23",
                endDate = "Oct 22",
                iconResId = R.drawable.libra // <-- IMPORTANT: Change to your drawable ID
            )
            val libraEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Libra",
                description = "Libra is the seventh astrological sign, known for its diplomatic, fair-minded, and social grace.",
                element = "Air",
                rulingPlanet = "Venus"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(libra, listOf(libraEn))

            // 8. Scorpio
            val scorpio = ZodiacSign(
                name = "Scorpio",
                startDate = "Oct 23",
                endDate = "Nov 21",
                iconResId = R.drawable.scorpio // <-- IMPORTANT: Change to your drawable ID
            )
            val scorpioEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Scorpio",
                description = "Scorpio is the eighth astrological sign, known for its passionate, resourceful, and mysterious aura.",
                element = "Water",
                rulingPlanet = "Pluto"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(scorpio, listOf(scorpioEn))

            // 9. Sagittarius
            val sagittarius = ZodiacSign(
                name = "Sagittarius",
                startDate = "Nov 22",
                endDate = "Dec 21",
                iconResId = R.drawable.sagittarius // <-- IMPORTANT: Change to your drawable ID
            )
            val sagittariusEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Sagittarius",
                description = "Sagittarius is the ninth astrological sign, known for its optimistic, adventurous, and philosophical outlook.",
                element = "Fire",
                rulingPlanet = "Jupiter"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(sagittarius, listOf(sagittariusEn))

            // 10. Capricorn
            val capricorn = ZodiacSign(
                name = "Capricorn",
                startDate = "Dec 22",
                endDate = "Jan 19",
                iconResId = R.drawable.capricorn // <-- IMPORTANT: Change to your drawable ID
            )
            val capricornEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Capricorn",
                description = "Capricorn is the tenth astrological sign, known for its disciplined, responsible, and ambitious drive.",
                element = "Earth",
                rulingPlanet = "Saturn"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(capricorn, listOf(capricornEn))

            // 11. Aquarius
            val aquarius = ZodiacSign(
                name = "Aquarius",
                startDate = "Jan 20",
                endDate = "Feb 18",
                iconResId = R.drawable.aquarius // <-- IMPORTANT: Change to your drawable ID
            )
            val aquariusEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Aquarius",
                description = "Aquarius is the eleventh astrological sign, known for its independent, humanitarian, and original ideas.",
                element = "Air",
                rulingPlanet = "Uranus"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(aquarius, listOf(aquariusEn))

            // 12. Pisces
            val pisces = ZodiacSign(
                name = "Pisces",
                startDate = "Feb 19",
                endDate = "Mar 20",
                iconResId = R.drawable.pisces // <-- IMPORTANT: Change to your drawable ID
            )
            val piscesEn = ZodiacSignTranslation(
                zodiacSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Pisces",
                description = "Pisces is the twelfth astrological sign, known for its compassionate, artistic, and intuitive nature.",
                element = "Water",
                rulingPlanet = "Neptune"
            )
            zodiacSignDao.insertZodiacSignWithTranslations(pisces, listOf(piscesEn))


            // -- Benefits ----------------------------------------


            // 1. Vitality
            val vitality = Benefit(name = "vitality", imageResId = R.drawable.stone_1)
            val vitalityEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Vitality"
            )
            benefitDao.insertBenefitWithTranslations(vitality, listOf(vitalityEn))

// 2. Mindfulness
            val mindfulness = Benefit(name = "mindfulness", imageResId = R.drawable.stone_2)
            val mindfulnessEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Mindfulness"
            )
            benefitDao.insertBenefitWithTranslations(mindfulness, listOf(mindfulnessEn))

// 3. Personal Growth
            val personalGrowth = Benefit(name = "personal_growth", imageResId = R.drawable.stone_3)
            val personalGrowthEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Personal Growth"
            )
            benefitDao.insertBenefitWithTranslations(personalGrowth, listOf(personalGrowthEn))

// 4. Anxiety & Depression
            val anxietyDepression =
                Benefit(name = "anxiety_and_depression", imageResId = R.drawable.stone_4)
            val anxietyDepressionEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Anxiety & Depression"
            )
            benefitDao.insertBenefitWithTranslations(anxietyDepression, listOf(anxietyDepressionEn))

// 5. Love Relationships
            val loveRelationships =
                Benefit(name = "love_relationships", imageResId = R.drawable.stone_5)
            val loveRelationshipsEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Love Relationships"
            )
            benefitDao.insertBenefitWithTranslations(loveRelationships, listOf(loveRelationshipsEn))

// 6. Anger & Stress Relief
            val angerStressRelief =
                Benefit(name = "anger_and_stress_relief", imageResId = R.drawable.stone_6)
            val angerStressReliefEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Anger & Stress Relief"
            )
            benefitDao.insertBenefitWithTranslations(angerStressRelief, listOf(angerStressReliefEn))

// 7. Transformation
            val transformation = Benefit(name = "transformation", imageResId = R.drawable.stone_7)
            val transformationEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Transformation"
            )
            benefitDao.insertBenefitWithTranslations(transformation, listOf(transformationEn))

// 8. Clarity & Focus
            val clarityFocus = Benefit(name = "clarity_and_focus", imageResId = R.drawable.stone_8)
            val clarityFocusEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Clarity & Focus"
            )
            benefitDao.insertBenefitWithTranslations(clarityFocus, listOf(clarityFocusEn))

// 9. Career & Money
            val careerMoney = Benefit(name = "career_and_money", imageResId = R.drawable.stone_4_r)
            val careerMoneyEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Career & Money"
            )
            benefitDao.insertBenefitWithTranslations(careerMoney, listOf(careerMoneyEn))

// 10. Protection
            val protection = Benefit(name = "protection", imageResId = R.drawable.stone_3_r)
            val protectionEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Protection"
            )
            benefitDao.insertBenefitWithTranslations(protection, listOf(protectionEn))

// 11. Self-Control
            val selfControl = Benefit(name = "self_control", imageResId = R.drawable.stone_2_r)
            val selfControlEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Self-Control"
            )
            benefitDao.insertBenefitWithTranslations(selfControl, listOf(selfControlEn))

// 12. Communication
            val communication = Benefit(name = "communication", imageResId = R.drawable.stone_1_r)
            val communicationEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Communication"
            )
            benefitDao.insertBenefitWithTranslations(communication, listOf(communicationEn))

// 13. Physical Health
            val physicalHealth =
                Benefit(name = "physical_health", imageResId = R.drawable.stone_8_r)
            val physicalHealthEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Physical Health"
            )
            benefitDao.insertBenefitWithTranslations(physicalHealth, listOf(physicalHealthEn))

// 14. Intuition
            val intuition = Benefit(name = "intuition", imageResId = R.drawable.stone_7_r)
            val intuitionEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Intuition"
            )
            benefitDao.insertBenefitWithTranslations(intuition, listOf(intuitionEn))

// 15. Enhanced Focus
            val enhancedFocus = Benefit(name = "enhanced_focus", imageResId = R.drawable.stone_6_r)
            val enhancedFocusEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Enhanced Focus"
            )
            benefitDao.insertBenefitWithTranslations(enhancedFocus, listOf(enhancedFocusEn))

// 16. Spirituality
            val spirituality = Benefit(name = "spirituality", imageResId = R.drawable.stone_5_r)
            val spiritualityEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Spirituality"
            )
            benefitDao.insertBenefitWithTranslations(spirituality, listOf(spiritualityEn))

// 17. Mental Health
            val mentalHealth = Benefit(name = "mental_health", imageResId = R.drawable.stone_5)
            val mentalHealthEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Mental Health"
            )
            benefitDao.insertBenefitWithTranslations(mentalHealth, listOf(mentalHealthEn))

// 18. Creativity
            val creativity = Benefit(name = "creativity", imageResId = R.drawable.stone_6)
            val creativityEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Creativity"
            )
            benefitDao.insertBenefitWithTranslations(creativity, listOf(creativityEn))

// 19. Good Fortune & Luck
            val goodFortuneLuck =
                Benefit(name = "good_fortune_and_luck", imageResId = R.drawable.stone_7)
            val goodFortuneLuckEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Good Fortune & Luck"
            )
            benefitDao.insertBenefitWithTranslations(goodFortuneLuck, listOf(goodFortuneLuckEn))

// 20. Manifestation
            val manifestation = Benefit(name = "manifestation", imageResId = R.drawable.stone_8)
            val manifestationEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Manifestation"
            )
            benefitDao.insertBenefitWithTranslations(manifestation, listOf(manifestationEn))

// 21. Emotional Balance
            val emotionalBalance =
                Benefit(name = "emotional_balance", imageResId = R.drawable.stone_4_r)
            val emotionalBalanceEn = BenefitTranslation(
                benefitId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Emotional Balance"
            )
            benefitDao.insertBenefitWithTranslations(emotionalBalance, listOf(emotionalBalanceEn))


            // 1. Rat
            val rat = ChineseZodiacSign(
                name = "Rat",
                iconResId = R.drawable.rat,
                iconResIdBorder = R.drawable.rat_border,
                iconResIdColor = R.drawable.rat_color,
                recentYears = "1960, 1972, 1984, 1996, 2008, 2020"
            )
            val ratEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Rat",
                description = "The Rat is the first sign, known for being quick-witted, resourceful, and charming.",
            )
            chineseDao.insertChineseSignWithTranslations(rat, listOf(ratEn))

            // 2. Ox
            val ox = ChineseZodiacSign(
                name = "Ox",
                iconResId = R.drawable.ox,
                iconResIdBorder = R.drawable.ox_border,
                iconResIdColor = R.drawable.ox_color,
                recentYears = "1961, 1973, 1985, 1997, 2009, 2021"
            )
            val oxEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Ox",
                description = "The Ox is the second sign, valued for its diligence, dependability, and determination."
            )
            chineseDao.insertChineseSignWithTranslations(ox, listOf(oxEn))

            // 3. Tiger
            val tiger = ChineseZodiacSign(
                name = "Tiger",
                iconResId = R.drawable.tiger,
                iconResIdBorder = R.drawable.tiger_border,
                iconResIdColor = R.drawable.tiger_color,
                recentYears = "1962, 1974, 1986, 1998, 2010, 2022"
            )
            val tigerEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Tiger",
                description = "The Tiger is the third sign, known for its bravery, confidence, and competitiveness."
            )
            chineseDao.insertChineseSignWithTranslations(tiger, listOf(tigerEn))

            // 4. Rabbit
            val rabbit = ChineseZodiacSign(
                name = "Rabbit",
                iconResId = R.drawable.rabbit,
                iconResIdBorder = R.drawable.rabbit_border,
                iconResIdColor = R.drawable.rabbit_color,
                recentYears = "1963, 1975, 1987, 1999, 2011, 2023"
            )
            val rabbitEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Rabbit",
                description = "The Rabbit is the fourth sign, representing elegance, kindness, and vigilance."
            )
            chineseDao.insertChineseSignWithTranslations(rabbit, listOf(rabbitEn))

            // 5. Dragon
            val dragon = ChineseZodiacSign(
                name = "Dragon",
                iconResId = R.drawable.dragon,
                iconResIdBorder = R.drawable.dragon_border,
                iconResIdColor = R.drawable.dragon_color,
                recentYears = "1964, 1976, 1988, 2000, 2012, 2024"
            )
            val dragonEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Dragon",
                description = "The Dragon is the fifth sign, a symbol of power, charisma, and ambition."
            )
            chineseDao.insertChineseSignWithTranslations(dragon, listOf(dragonEn))

            // 6. Snake
            val snake = ChineseZodiacSign(
                name = "Snake",
                iconResId = R.drawable.snake,
                iconResIdBorder = R.drawable.snake_border,
                iconResIdColor = R.drawable.snake_color,
                recentYears = "1965, 1977, 1989, 2001, 2013, 2025"
            )
            val snakeEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Snake",
                description = "The Snake is the sixth sign, known for its intelligence, intuition, and mysterious nature."
            )
            chineseDao.insertChineseSignWithTranslations(snake, listOf(snakeEn))

            // 7. Horse
            val horse = ChineseZodiacSign(
                name = "Horse",
                iconResId = R.drawable.horse,
                iconResIdBorder = R.drawable.horse_border,
                iconResIdColor = R.drawable.horse_color,
                recentYears = "1966, 1978, 1990, 2002, 2014, 2026"
            )
            val horseEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Horse",
                description = "The Horse is the seventh sign, representing energy, independence, and a free spirit."
            )
            chineseDao.insertChineseSignWithTranslations(horse, listOf(horseEn))

            // 8. Goat
            val goat = ChineseZodiacSign(
                name = "Goat",
                iconResId = R.drawable.goat,
                iconResIdBorder = R.drawable.goat_border,
                iconResIdColor = R.drawable.goat_color,
                recentYears = "1967, 1979, 1991, 2003, 2015, 2027"
            )
            val goatEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Goat",
                description = "The Goat is the eighth sign, known for its gentle, compassionate, and artistic nature."
            )
            chineseDao.insertChineseSignWithTranslations(goat, listOf(goatEn))

            // 9. Monkey
            val monkey = ChineseZodiacSign(
                name = "Monkey",
                iconResId = R.drawable.monkey,
                iconResIdBorder = R.drawable.monkey_border,
                iconResIdColor = R.drawable.monkey_color,
                recentYears = "1968, 1980, 1992, 2004, 2016, 2028"
            )
            val monkeyEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Monkey",
                description = "The Monkey is the ninth sign, characterized by its wit, curiosity, and playful personality."
            )
            chineseDao.insertChineseSignWithTranslations(monkey, listOf(monkeyEn))

            // 10. Rooster
            val rooster = ChineseZodiacSign(
                name = "Rooster",
                iconResId = R.drawable.rooster,
                iconResIdBorder = R.drawable.rooster_border,
                iconResIdColor = R.drawable.rooster_color,
                recentYears = "1969, 1981, 1993, 2005, 2017, 2029"
            )
            val roosterEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Rooster",
                description = "The Rooster is the tenth sign, known for being observant, hardworking, and courageous."
            )
            chineseDao.insertChineseSignWithTranslations(rooster, listOf(roosterEn))

            // 11. Dog
            val dog = ChineseZodiacSign(
                name = "Dog",
                iconResId = R.drawable.dog,
                iconResIdBorder = R.drawable.dog_border,
                iconResIdColor = R.drawable.dog_color,
                recentYears = "1970, 1982, 1994, 2006, 2018, 2030"
            )
            val dogEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Dog",
                description = "The Dog is the eleventh sign, valued for its loyalty, honesty, and strong sense of justice."
            )
            chineseDao.insertChineseSignWithTranslations(dog, listOf(dogEn))

            // 12. Pig
            val pig = ChineseZodiacSign(
                name = "Pig",
                iconResId = R.drawable.pig,
                iconResIdBorder = R.drawable.pig_border,
                iconResIdColor = R.drawable.pig_color,
                recentYears = "1971, 1983, 1995, 2007, 2019, 2031"
            )
            val pigEn = ChineseZodiacSignTranslation(
                chineseSignId = 0,
                languageCode = LanguageCode.ENGLISH,
                name = "Pig",
                description = "The Pig is the twelfth sign, known for its compassion, generosity, and diligence."
            )
            chineseDao.insertChineseSignWithTranslations(pig, listOf(pigEn))

            Log.d("AppDatabase", "Finished populating Chinese Zodiac signs.")
        }
    }
}