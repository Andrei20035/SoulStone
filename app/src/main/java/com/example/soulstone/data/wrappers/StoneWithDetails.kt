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
        parentColumn = "id", // Key of the parent (Stone)
        entity = ZodiacSign::class,
        entityColumn = "id", // Key of the child (ZodiacSign)
        associateBy = Junction(
            value = StoneZodiacCrossRef::class,
            parentColumn = "stoneId",  // <-- Column in junction for the Stone
            entityColumn = "zodiacSignId" // <-- Column in junction for the ZodiacSign
        )
    )
    val zodiacSigns: List<ZodiacSign>,

    @Relation(
        parentColumn = "id",
        entity = ChineseZodiacSign::class,
        entityColumn = "id",
        associateBy = Junction(
            value = StoneChineseZodiacCrossRef::class,
            parentColumn = "stoneId",  // <-- Column in junction for the Stone
            entityColumn = "chineseZodiacSignId" // <-- Column in junction for the ChineseZodiacSign
        )
    )
    val chineseZodiacSigns: List<ChineseZodiacSign>,

    @Relation(
        parentColumn = "id",
        entity = Benefit::class,
        entityColumn = "id",
        associateBy = Junction(
            value = StoneBenefitCrossRef::class,
            parentColumn = "stoneId",  // <-- Column in junction for the Stone
            entityColumn = "benefitId" // <-- Column in junction for the Benefit
        )
    )
    val benefits: List<Benefit>,

    @Relation(
        parentColumn = "id",
        entity = Chakra::class,
        entityColumn = "id",
        associateBy = Junction(
            value = StoneChakraCrossRef::class,
            parentColumn = "stoneId",  // <-- Column in junction for the Stone
            entityColumn = "chakraId" // <-- Column in junction for the Chakra
        )
    )
    val chakras: List<Chakra>
)

