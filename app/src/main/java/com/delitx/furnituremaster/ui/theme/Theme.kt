package com.delitx.furnituremaster.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.delitx.furnituremaster.R

val MainOrange = Color(0xFF35D6E8)
val OrangeVariant = Color(0xFF53c2cf)
val LightSecondary = Color(0xFF46f0e4)
val LightSecondaryVariant = Color(0xFF6fe8e0)
val Error = Color(0xFFfa4632)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

val Dark = Color(0xFF121212)

val lightThemeColors = Colors(
    primary = MainOrange,
    primaryVariant = OrangeVariant,
    secondary = LightSecondary,
    secondaryVariant = LightSecondaryVariant,
    background = White,
    surface = White,
    error = Error,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = Black,
    isLight = true
)
val darkThemeColors = Colors(
    primary = MainOrange,
    primaryVariant = OrangeVariant,
    secondary = LightSecondary,
    secondaryVariant = LightSecondaryVariant,
    background = Black,
    surface = Dark,
    error = Error,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White,
    isLight = false
)
val proximaNovaFontFamily = FontFamily(
    Font(R.font.proxima_nova_light, FontWeight.Light),
    Font(R.font.proxima_nova_bold, FontWeight.Bold),
    Font(R.font.proxima_nova_regular, FontWeight.Normal),
    Font(R.font.proxima_nova_regular_italic, FontWeight.Normal, FontStyle.Italic)
)

@Composable
fun FurnitureMasterTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkTheme) darkThemeColors else lightThemeColors,
        typography = Typography(defaultFontFamily = proximaNovaFontFamily)
    ) {
        content()
    }
}
