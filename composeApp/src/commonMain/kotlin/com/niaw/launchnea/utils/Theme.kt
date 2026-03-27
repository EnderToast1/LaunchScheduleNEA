package com.niaw.launchnea.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

@Composable
fun AppTheme(
    selectedMode: ThemeMode,
    seedColourType: ColourTheme,
    content: @Composable () -> Unit
) {
    val seedColour = seedColourType.getSeedColour()
    val isDark = when (selectedMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = remember(seedColour, isDark) {
        generateColorScheme(seedColour, isDark)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}



fun generateColorScheme(seed: Color, isDark: Boolean): ColorScheme {

    return if (isDark) {
        darkColorScheme(
            primary = tonal(seed, 0.7f),
            onPrimary = tonal(seed, 0.1f),

            primaryContainer = tonal(seed, 0.3f),
            onPrimaryContainer = tonal(seed, 0.9f),

            secondary = tonal(seed, 0.6f, 0.8f),
            onSecondary = tonal(seed, 0.1f),

            secondaryContainer = tonal(seed, 0.3f, 0.8f),
            onSecondaryContainer = tonal(seed, 0.9f, 0.8f),

            tertiary = tonal(seed, 0.6f, 0.6f),
            onTertiary = tonal(seed, 0.1f, 0.6f),

            tertiaryContainer = tonal(seed, 0.3f, 0.6f),
            onTertiaryContainer = tonal(seed, 0.9f, 0.6f),

            background = Color(0xFF121212),
            onBackground = Color.White,

            surface = Color(0xFF1E1E1E),
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = tonal(seed, 0.4f),
            onPrimary = Color.White,

            primaryContainer = tonal(seed, 0.9f),
            onPrimaryContainer = tonal(seed, 0.2f),

            secondary = tonal(seed, 0.5f, 0.8f),
            onSecondary = Color.White,

            secondaryContainer = tonal(seed, 0.9f, 0.8f),
            onSecondaryContainer = tonal(seed, 0.2f, 0.8f),

            tertiary = tonal(seed, 0.6f, 0.6f),

            tertiaryContainer = tonal(seed, 0.9f, 0.6f),
            onTertiaryContainer = tonal(seed, 0.2f, 0.6f),

            background = Color(0xFFF8F9FA),
            onBackground = Color.Black,

            surface = Color.White,
            onSurface = Color.Black
        )
    }
}

fun tonal(seed: Color, lightness: Float, saturationFactor: Float = 1f): Color {
    val hsl = seed.toHSL()
    return HSL(
        h = hsl.h,
        s = (hsl.s * saturationFactor).coerceIn(0f, 1f),
        l = lightness.coerceIn(0f, 1f)
    ).toColor()
}

data class HSL(val h: Float, val s: Float, val l: Float)

fun Color.toHSL(): HSL {
    val r = red
    val g = green
    val b = blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    val l = (max + min) / 2f
    val s = if (delta == 0f) 0f else delta / (1f - abs(2f * l - 1f))

    var h = 0f
    if (delta != 0f) {
        h = when (max) {
            r -> (g - b) / delta + (if (g < b) 6f else 0f)
            g -> (b - r) / delta + 2f
            b -> (r - g) / delta + 4f
            else -> 0f
        }
        h *= 60f
    }

    return HSL(h, s, l)
}

fun HSL.toColor(): Color {
    val c = (1f - abs(2 * l - 1)) * s
    val x = c * (1 - abs((h / 60f) % 2 - 1))
    val m = l - c / 2

    val (r1, g1, b1) = when {
        h < 60 -> Triple(c, x, 0f)
        h < 120 -> Triple(x, c, 0f)
        h < 180 -> Triple(0f, c, x)
        h < 240 -> Triple(0f, x, c)
        h < 300 -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color(r1 + m, g1 + m, b1 + m, 1f)
}




fun ColourTheme.getSeedColour(): Color {
    return  (Color(0xFF6750A4))
}