package com.example.soulstone.data.entities

import androidx.room.ColumnInfo
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
    val description: String,
    val location: String,

    val rulingPlanet: String,
    val element: String,
    val stoneColors: String,
    val healingQualities: String,
    val stones: String,
    val bodyPlacement: String,
    val housePlacement: String,
    val herbs: String,
    val essentialOils: String
)
