package com.example.soulstone.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["stoneId", "zodiacSignId"])
data class StoneZodiacCrossRef(
    val stoneId: Int,
    val zodiacSignId: Int
)
