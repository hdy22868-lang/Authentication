package com.example.authentication.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.authentication.ui.theme.LocalAuthAssets
import kotlin.random.Random

@Composable
fun AuthBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val assets = LocalAuthAssets.current
    val colors = MaterialTheme.colorScheme
    val isDark = colors.background.luminance() < 0.5f
    val pool = if (isDark) assets.darkBackgrounds else assets.lightBackgrounds

    val index = rememberSaveable(isDark, pool.size) {
        if (pool.isEmpty()) 0 else Random.nextInt(pool.size)
    }
    val selected = pool.getOrNull(index)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colors.background,
        contentColor = colors.onBackground
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (selected != null) {
                Image(
                    painter = painterResource(id = selected),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background.copy(alpha = assets.backgroundScrimAlpha))
                )
            }
            content()
        }
    }
}