package com.niaw.launchnea.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    selectedMode: ThemeMode,
    content: @Composable () -> Unit
) {




    // Determine the actual boolean for dark mode
    val isDark = when (selectedMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()




    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}