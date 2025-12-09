package com.example.soulstone.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.soulstone.util.LanguageCode

@Entity(
    tableName = "chinese_sign_translations",
    foreignKeys = [ForeignKey(
        entity = ChineseZodiacSign::class,
        parentColumns = ["id"],
        childColumns = ["chineseSignId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["chineseSignId", "languageCode"], unique = true)]
)
data class ChineseZodiacSignTranslation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chineseSignId: Int,
    val languageCode: LanguageCode,

    val name: String,
    val description: String,
    val traits: String,
    val bestMatch: String,
    val worstMatch: String,
    val compatibilityDescription: String,
    val gemstoneDescription: String
)
