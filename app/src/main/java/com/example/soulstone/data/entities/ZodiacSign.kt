package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "zodiac_signs",
    indices = [Index(value = ["name"], unique = true)]
)
data class ZodiacSign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startDate: String,
    val endDate: String,
    val imageName: String
)
