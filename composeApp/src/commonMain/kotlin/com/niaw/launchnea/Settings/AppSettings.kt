package com.niaw.launchnea.Settings

import androidx.compose.ui.graphics.Color
import com.niaw.launchnea.utils.ColourTheme
import com.niaw.launchnea.utils.DateFormat
import com.niaw.launchnea.utils.ThemeMode
import com.niaw.launchnea.utils.TimeFormat
import com.russhwolf.settings.Settings

class AppSettings(private val settings: Settings) {
    private val themeKey = "selected_theme"
    private val colourThemeKey = "selected_colour_theme"
    private val timeFormatKey = "selected_time_format"
    private val dateFormatKey = "selected_date_format"
    private val colourSuccessKey = "selected_colour_success"
    private val colourPartialFailureKey = "selected_colour_partial_failure"
    private val colourFailureKey = "selected_colour_failure"
    private val colourInFlightKey = "selected_colour_in_flight"
    private val colourHoldKey = "selected_colour_hold"

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

    //Success Type Colour
        //Success
    fun saveColourSuccess(successColor: Color) {settings.putString(colourSuccessKey, successColor.value.toString())}
    fun getColourSuccess(): Color {
        val saved = settings.getString(colourSuccessKey, Color.Green.value.toString())
        return Color(saved.toULong())
    }
        //Partial Failure
    fun saveColourPartialFailure(successColor: Color) {settings.putString(colourPartialFailureKey, successColor.value.toString())}
    fun getColourPartialFailure(): Color {
        val saved = settings.getString(colourPartialFailureKey, Color.Yellow.value.toString())
        return Color(saved.toULong())
    }
        //Failure
    fun saveColourFailure(successColor: Color) {settings.putString(colourFailureKey, successColor.value.toString())}
    fun getColourFailure(): Color {
        val saved = settings.getString(colourFailureKey, Color.Red.value.toString())
        return Color(saved.toULong())
    }
        //In Flight
    fun saveColourInFlight(successColor: Color) {settings.putString(colourInFlightKey, successColor.value.toString())}
    fun getColourInFlight(): Color {
        val saved = settings.getString(colourInFlightKey, Color.Blue.value.toString())
        return Color(saved.toULong())
    }

        //Hold
    fun saveColourHold(successColor: Color) {settings.putString(colourHoldKey, successColor.value.toString())}
    fun getColourHold(): Color {
        val saved = settings.getString(colourHoldKey, Color(0xFFFF6600).value.toString())
        return Color(saved.toULong())
    }

}