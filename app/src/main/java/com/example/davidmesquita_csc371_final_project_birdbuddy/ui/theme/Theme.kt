package com.example.davidmesquita_csc371_final_project_birdbuddy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PurplePrimary = Color(0xFF8A3FFC)
private val PurpleDark = Color(0xFF3D2E79)
private val PurpleLight = Color(0xFFDCCBFF)
private val MagentaAccent = Color(0xFFFF2E93)
private val PinkViolet = Color(0xFFD74FFF)
private val BackgroundLight = Color(0xFFF7F2FF)
private val CardSurface = Color(0xFFFFFFFF)
private val TextPrimaryDark = Color(0xFF1A1528)
private val ErrorRed = Color(0xFFB3261E)

private val LightColors = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleLight,
    onPrimaryContainer = PurpleDark,

    secondary = PinkViolet,
    onSecondary = Color.White,

    tertiary = MagentaAccent,
    onTertiary = Color.White,

    background = BackgroundLight,
    onBackground = TextPrimaryDark,

    surface = CardSurface,
    onSurface = TextPrimaryDark,

    error = ErrorRed,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = PurpleLight,
    onPrimary = PurpleDark,
    background = Color(0xFF0D0A1A),
    onBackground = Color(0xFFEFE6FF),
    surface = Color(0xFF1A1530),
    onSurface = Color(0xFFEFE6FF)
)

val BirdBuddyTypography = Typography(
    headlineLarge = Typography().headlineLarge.copy(fontSize = 30.sp),
    headlineMedium = Typography().headlineMedium.copy(fontSize = 24.sp),
    headlineSmall = Typography().headlineSmall.copy(fontSize = 20.sp),
    titleMedium = Typography().titleMedium.copy(letterSpacing = 0.3.sp),
    bodyLarge = Typography().bodyLarge.copy(lineHeight = 22.sp),
    bodyMedium = Typography().bodyMedium.copy(lineHeight = 20.sp)
)

val BirdBuddyShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp)
)

@Composable
fun BirdBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = BirdBuddyTypography,
        shapes = BirdBuddyShapes,
        content = content
    )
}
