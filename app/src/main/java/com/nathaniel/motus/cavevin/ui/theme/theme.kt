package com.nathaniel.motus.cavevin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColors = lightColors(
    primary = brown700,
    primaryVariant = brown400,
    secondary = green900,
    secondaryVariant = greenCustomDark,
    background = pureWhite,
    surface = pureWhite,
    onPrimary = pureWhite,
    onSecondary = pureWhite
)

private val DarkColors = darkColors(
    primary = brown900,
    primaryVariant = brown700,
    secondary = green900,
    secondaryVariant = greenCustomDark,
    background = pureBlack,
    surface = pureBlack,
    onPrimary = pureWhite,
    onSecondary = pureWhite
)

@Composable
fun WineCellarMainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}