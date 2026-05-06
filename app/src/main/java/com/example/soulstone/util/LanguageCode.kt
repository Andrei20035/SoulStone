package com.example.soulstone.util

import androidx.annotation.DrawableRes
import com.example.soulstone.R

enum class LanguageCode(val code: String, @DrawableRes val flagResId: Int) {
    ENGLISH("en", R.drawable.flag_uk),
    SPANISH("es", R.drawable.flag_es),
    FRENCH("fr", R.drawable.flag_fr),
    ITALIAN("it", R.drawable.flag_it),
    GERMAN("de", R.drawable.flag_de),
    POLISH("pl", R.drawable.flag_pl),
    RUSSIAN("ru", R.drawable.flag_ru);

    companion object {
        fun fromCode(code: String?): LanguageCode {
            return entries.find {it.code == code } ?: ENGLISH
        }
    }
}