package com.example.soulstone.data.wrappers

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.soulstone.data.entities.Benefit
import com.example.soulstone.data.entities.Chakra
import com.example.soulstone.data.entities.ChineseZodiacSign
import com.example.soulstone.data.entities.Stone
import com.example.soulstone.data.entities.StoneTranslation
import com.example.soulstone.data.entities.ZodiacSign
import com.example.soulstone.data.relations.StoneBenefitCrossRef
import com.example.soulstone.data.relations.StoneChakraCrossRef
import com.example.soulstone.data.relations.StoneChineseZodiacCrossRef
import com.example.soulstone.data.relations.StoneZodiacCrossRef

data class StoneWithDetails(
    @Embedded val stone: Stone,

    @Relation(
        parentColumn = "id",
        entityColumn = "stoneId"
    )
    val translations: List<StoneTranslation>,

    @Relation(
        parentColumn = "id",
        entity = ZodiacSign::class,
        entityColumn = "id",
        associateBy = Junction(StoneZodiacCrossRef::class)
    )
    val zodiacSigns: List<ZodiacSign>,

    @Relation(
        parentColumn = "id",
        entity = ChineseZodiacSign::class,
        entityColumn = "id",
        associateBy = Junction(StoneChineseZodiacCrossRef::class)
    )
    val chineseZodiacSigns: List<ChineseZodiacSign>,

    @Relation(
        parentColumn = "id",
        entity = Benefit::class,
        entityColumn = "id",
        associateBy = Junction(StoneBenefitCrossRef::class)
    )
    val benefits: List<Benefit>,

    @Relation(
        parentColumn = "id",
        entity = Chakra::class,
        entityColumn = "id",
        associateBy = Junction(StoneChakraCrossRef::class)
    )
    val chakras: List<Chakra>
)

