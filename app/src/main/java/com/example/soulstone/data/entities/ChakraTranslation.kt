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

    @ColumnInfo(name = "ruling_planet")
    val rulingPlanet: String,
    val element: String,

    @ColumnInfo(name = "stone_colors")
    val stoneColors: String,

    @ColumnInfo(name = "healing_qualities")
    val healingQualities: String,
    val stones: String,

    @ColumnInfo(name = "body_placement")
    val bodyPlacement: String,

    @ColumnInfo(name = "house_placement")
    val housePlacement: String,
    val herbs: String,

    @ColumnInfo(name = "essential_oils")
    val essentialOils: String
)
