package com.example.soulstone.data.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "stone_zodiac_cross_ref",
    primaryKeys = ["stoneId", "zodiacSignId"],
    indices = [Index(value = ["zodiacSignId"])]
)
data class StoneZodiacCrossRef(
    val stoneId: Int,
    val zodiacSignId: Int
)
