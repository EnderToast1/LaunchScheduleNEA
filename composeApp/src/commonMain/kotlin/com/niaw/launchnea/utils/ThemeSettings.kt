package com.niaw.launchnea.utils

import com.russhwolf.settings.Settings

class ThemeSettings(private val settings: Settings) {
    private val themeKey = "selected_theme"

    fun saveTheme(mode: ThemeMode) {
        settings.putString(themeKey, mode.name)
    }

    fun getTheme(): ThemeMode {
        val saved = settings.getString(themeKey, ThemeMode.System.name)
        return ThemeMode.valueOf(saved)
    }
}
