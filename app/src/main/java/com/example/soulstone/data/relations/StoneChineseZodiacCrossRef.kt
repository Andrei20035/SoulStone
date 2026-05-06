package com.example.soulstone.data.relations

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "stone_chinese_zodiac_cross_ref",
    primaryKeys = ["stoneId", "chineseZodiacSignId"],
    indices = [Index(value = ["chineseZodiacSignId"])]
)
data class StoneChineseZodiacCrossRef(
    val stoneId: Int,
    val chineseZodiacSignId: Int
)
