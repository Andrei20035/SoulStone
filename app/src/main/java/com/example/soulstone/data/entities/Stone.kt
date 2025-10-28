package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stones",
    indices = [Index(value = ["name"], unique = true)]
)
data class Stone(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String? = null // TODO: Save the image to files before saving the Uri(Uri can be temporary)
)
