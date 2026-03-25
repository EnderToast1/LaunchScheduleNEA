package com.niaw.launchnea.utils

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings) {
    private val themeKey = "selected_theme"
    private val timeFormatKey = "selected_time_format"
    private val dateFormatKey = "selected_date_format"

    fun saveTheme(mode: ThemeMode) {
        settings.putString(themeKey, mode.name)
    }

    fun getTheme(): ThemeMode {
        val saved = settings.getString(themeKey, ThemeMode.System.name)
        return ThemeMode.valueOf(saved)
    }

    fun saveTimeFormat(format: TimeFormat) {
        settings.putString(timeFormatKey, format.name)
    }

    fun getTimeFormat(): TimeFormat {
        val saved = settings.getString(timeFormatKey, TimeFormat.HOUR24SeOFF.name)
        return TimeFormat.valueOf(saved)
    }

    fun saveDateFormat(format: DateFormat) {
        settings.putString(dateFormatKey, format.name)
    }

    fun getDateFormat(): DateFormat {
        val saved = settings.getString(dateFormatKey, DateFormat.DayNMonthYear.name)
        return DateFormat.valueOf(saved)
    }


}
