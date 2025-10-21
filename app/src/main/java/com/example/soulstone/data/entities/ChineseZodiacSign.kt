package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chinese_zodiac_signs")
data class ChineseZodiacSign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
