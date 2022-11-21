package com.ivancaas.beersapp.presentation.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xff181a26), //NAVBAR y BottomBar
    primaryVariant = Titles, //Titles Color
    secondary = Color.White,
    secondaryVariant = Color(0xffff9800), //TOOLTIPS
    onSecondary = Color.White, //TEXT COLOR
    background = Color(0xff28293d), //BACKGROUND
    surface = Color(0XFF595d79), //BORDER COLOR CARDS
    onBackground = Color(0xff323549), //CARD BACKGROUND
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color.White, //NAVBAR y BottomBar
    primaryVariant = Titles, //Titles Color
    secondary = Color.White,
    secondaryVariant = Color(0xffff9800), //TOOLTIPS
    onSecondary = Color.Black, //TEXT COLOR
    background = BackgroundColor, //BACKGROUND
    surface = Color(0XFF595d79), //BORDER COLOR CARDS
    onBackground = Color.White, //CARD BACKGROUND
)

@Composable
fun BaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(colors = colors, typography = Typography, shapes = Shapes, content = content)
}