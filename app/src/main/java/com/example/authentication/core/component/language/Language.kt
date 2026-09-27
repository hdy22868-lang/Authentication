package com.example.authentication.core.component.language

enum class Language(val code: String) {
    ARABIC("ar"),
    ENGLISH("en");

    companion object {
        fun fromCode(code: String): Language {
            return if (code.contains("en", ignoreCase = true)) ENGLISH else ARABIC
        }
    }
}