package com.example.soulstone.data.entities

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chinese_zodiac_signs",
    indices = [Index(value = ["name"], unique = true)]
)
data class ChineseZodiacSign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val recentYears: String,
    @DrawableRes val iconResId: Int
)
