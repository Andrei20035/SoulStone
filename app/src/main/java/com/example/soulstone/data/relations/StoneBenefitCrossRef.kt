package com.example.soulstone.data.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["stoneId", "benefitId"],
    indices = [Index(value = ["benefitId"])],
)
data class StoneBenefitCrossRef(
    val stoneId: Int,
    val benefitId: Int
)
