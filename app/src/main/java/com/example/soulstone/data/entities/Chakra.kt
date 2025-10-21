package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chakras")
data class Chakra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
