package com.example.soulstone.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["stoneId", "chineseZodiacSignId"])
data class StoneChineseZodiacCrossRef(
    val stoneId: Int,
    val chineseZodiacSignId: Int
)
