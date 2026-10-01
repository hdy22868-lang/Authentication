package com.example.authentication.core.component.language

enum class Language(val code: String, val displayName: String) {
    ARABIC("ar","العربية"),
    ENGLISH("en","English");

    companion object {
        fun fromCode(code: String): Language {
            return if (code.contains("en", ignoreCase = true)) ENGLISH else ARABIC
        }
    }
}