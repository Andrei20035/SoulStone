package com.example.soulstone.data.converters

import androidx.room.TypeConverter
import com.example.soulstone.util.LanguageCode

class Converters {
    @TypeConverter
    fun fromLanguageCode(languageCode: LanguageCode?): String? = languageCode?.code

    @TypeConverter
    fun toLanguageCode(code: String?): LanguageCode {
        return LanguageCode.entries.find { it.code == code }
            ?: LanguageCode.ENGLISH
    }
}