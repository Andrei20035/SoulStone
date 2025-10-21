package com.example.soulstone.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["stoneId", "chakraId"])
data class StoneChakraCrossRef(
    val stoneId: Int,
    val chakraId: Int
)
