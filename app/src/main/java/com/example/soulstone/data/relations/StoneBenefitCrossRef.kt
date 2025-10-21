package com.example.soulstone.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["stoneId", "benefitId"])
data class StoneBenefitCrossRef(
    val stoneId: Int,
    val benefitId: Int
)
