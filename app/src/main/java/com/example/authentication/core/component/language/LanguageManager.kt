package com.example.authentication.core.component.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageManager {
    val currentLanguage: Language
        get() {
            val tags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            val languageCode = tags.ifBlank {
                Locale.getDefault().language
            }
            return Language.fromCode(languageCode)
        }

    fun switchLanguage(languageCode: String?) {
        val localeList = if (languageCode.isNullOrBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageCode)
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}