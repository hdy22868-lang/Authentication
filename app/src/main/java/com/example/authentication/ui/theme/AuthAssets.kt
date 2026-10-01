package com.example.authentication.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AuthAssets(
    @DrawableRes val welcomeImage: Int? = null,
    @DrawableRes val loginImage: Int? = null,
    @DrawableRes val registerImage: Int? = null,
    val lightBackgrounds: List<Int> = emptyList(),
    val darkBackgrounds: List<Int> = emptyList(),
    val backgroundScrimAlpha: Float = 0.7f
)

val LocalAuthAssets = staticCompositionLocalOf { AuthAssets() }