package com.example.soulstone.util

enum class LanguageCode(val code: String) {
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    ITALIAN("it"),
    GERMAN("de"),
    POLISH("pl"),
    RUSSIAN("ru");

    companion object {
        fun fromCode(code: String?): LanguageCode {
            return entries.find {it.code == code } ?: ENGLISH
        }
    }
}