package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.data.model.LanguageCode


@Entity(
    tableName = "zodiac_translations",
    foreignKeys = [ForeignKey(
        entity = ZodiacSign::class,
        parentColumns = ["id"],
        childColumns = ["zodiacSignId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["zodiacSignId", "languageCode"], unique = true)]
)
data class ZodiacSignTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val zodiacSignId: Int,
    val languageCode: LanguageCode,
    val name: String,
    val description: String,
    val element: String,
    val rulingPlanet: String
)