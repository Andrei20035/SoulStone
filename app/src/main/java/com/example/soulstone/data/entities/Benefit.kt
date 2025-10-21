package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "benefits")
data class Benefit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
