package com.example.soulstone.data.entities

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chakras",
    indices = [Index(value = ["sanskritName"], unique = true)]
)
data class Chakra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sanskritName: String,
    @DrawableRes val iconResId: Int

)
