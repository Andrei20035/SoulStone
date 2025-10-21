package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.data.model.LanguageCode

@Entity(
    tableName = "stone_translations",
    foreignKeys = [ForeignKey(
        entity = Stone::class,
        parentColumns = ["id"],
        childColumns = ["stoneId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("stoneId")]
)
data class StoneTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stoneId: Int,
    val languageCode: LanguageCode,
    val name: String,
    val description: String
)
