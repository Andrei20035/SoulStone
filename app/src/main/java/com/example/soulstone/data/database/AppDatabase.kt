package com.example.soulstone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.soulstone.data.converters.Converters
import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef
import com.example.soulstone.domain.model.Chakra
import com.example.soulstone.domain.model.ChineseZodiacSign
import com.example.soulstone.domain.model.Stone
import com.example.soulstone.domain.model.ZodiacSign

@Database(
    entities = [
        Stone::class,
        StoneTranslation::class,
        ZodiacSign::class,
        ChineseZodiacSign::class,
        Benefit::class,
        Chakra::class,
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
}