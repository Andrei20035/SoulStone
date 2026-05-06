package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.util.LanguageCode

@Entity(
    tableName = "stone_translations",
    foreignKeys = [ForeignKey(
        entity = Stone::class,
        parentColumns = ["id"],
        childColumns = ["stoneId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["stoneId", "languageCode"], unique = true)]
)
data class StoneTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stoneId: Int,
    val languageCode: LanguageCode,
    val name: String,
    val description: String
)
