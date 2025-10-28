package com.example.soulstone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
}