package com.example.soulstone.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.data.model.LanguageCode

@Entity(
    tableName = "benefit_translations",
    foreignKeys = [ForeignKey(
        entity = Benefit::class,
        parentColumns = ["id"],
        childColumns = ["benefitId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["benefitId", "languageCode"], unique = true)]
)
data class BenefitTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val benefitId: Int,
    val languageCode: LanguageCode,
    val name: String
)

