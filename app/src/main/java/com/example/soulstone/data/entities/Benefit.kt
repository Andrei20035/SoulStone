package com.example.soulstone.data.entities

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "benefits",
    indices = [Index(value = ["name"], unique = true)]
)
data class Benefit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @DrawableRes val imageResId: Int

)
