package com.niaw.launchnea.utils

import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings) {
    private val themeKey = "selected_theme"
    private val colourThemeKey = "selected_colour_theme"
    private val timeFormatKey = "selected_time_format"
    private val dateFormatKey = "selected_date_format"
    private val colourSuccessKey = "selected_colour_success"
    private val colourPartialFailureKey = "selected_colour_partial_failure"
    private val colourFailureKey = "selected_colour_failure"

    //Theme dark/light/system
    fun saveTheme(mode: ThemeMode) {settings.putString(themeKey, mode.name)}
    fun getTheme(): ThemeMode {
        val saved = settings.getString(themeKey, ThemeMode.System.name)
        return ThemeMode.valueOf(saved)
    }

    //Colour Theme
    fun saveColourTheme(colourTheme: ColourTheme) {settings.putString(colourThemeKey, colourTheme.name)}
    fun getColourTheme(): ColourTheme {
        val saved = settings.getString(colourThemeKey, ColourTheme.MAGENTA.name )
        return ColourTheme.valueOf(saved)
    }

    //Date Time formating
    fun saveTimeFormat(format: TimeFormat) {settings.putString(timeFormatKey, format.name)}
    fun getTimeFormat(): TimeFormat {
        val saved = settings.getString(timeFormatKey, TimeFormat.HOUR24SeOFF.name)
        return TimeFormat.valueOf(saved)
    }
    fun saveDateFormat(format: DateFormat) {settings.putString(dateFormatKey, format.name)}
    fun getDateFormat(): DateFormat {
        val saved = settings.getString(dateFormatKey, DateFormat.DayNMonthYear.name)
        return DateFormat.valueOf(saved)
    }


}
