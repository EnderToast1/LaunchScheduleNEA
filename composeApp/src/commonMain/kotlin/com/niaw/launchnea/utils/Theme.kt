package com.niaw.launchnea.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.niaw.launchnea.Settings.getSeedColour
import dev.teogor.paletteon.PaletteStyle
import dev.teogor.paletteon.PaletteonTheme

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
    var colourScheme= PaletteonTheme(
        seedColor = seedColour,
        useDarkTheme = isDark,
        ).colorScheme

    if (seedColourType== ColourTheme.MONOCHROME){
        colourScheme = PaletteonTheme(
            seedColor=seedColour,
            useDarkTheme = isDark,
            style = PaletteStyle.Monochrome,

        ).colorScheme
    }

    MaterialTheme(
        colorScheme = colourScheme,
        content = content
    )
}




