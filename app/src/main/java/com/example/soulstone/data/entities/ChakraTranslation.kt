package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.util.LanguageCode


@Entity(
    tableName = "chakra_translations",
    foreignKeys = [ForeignKey(
        entity = Chakra::class,
        parentColumns = ["id"],
        childColumns = ["chakraId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["chakraId", "languageCode"], unique = true)]
)
data class ChakraTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chakraId: Int,
    val languageCode: LanguageCode,
    val name: String,
    val rulingPlanet: String,
    val color: String,
    val location: String,
    val description: String
)
