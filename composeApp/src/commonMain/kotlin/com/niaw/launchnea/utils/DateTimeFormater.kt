package com.niaw.launchnea.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

fun TimeFormat.toPattern(): String = when (this) {
    TimeFormat.HOUR12SeON  -> "hh:mm:ss a"
    TimeFormat.HOUR24SeON  -> "HH:mm:ss"
    TimeFormat.HOUR12SeOFF -> "hh:mm a"
    TimeFormat.HOUR24SeOFF -> "HH:mm"
}

fun String.toTimeFormat(): TimeFormat = when (this) {
    "hh:mm:ss a" -> TimeFormat.HOUR12SeON
    "HH:mm:ss"   -> TimeFormat.HOUR24SeON
    "hh:mm a"    -> TimeFormat.HOUR12SeOFF
    "HH:mm"      -> TimeFormat.HOUR24SeOFF
    else         -> TimeFormat.HOUR24SeOFF // fallback default
}

fun TimeFormat.toLabel(): String = when(this) {
    TimeFormat.HOUR12SeON -> "12-Hour (With Seconds)"
    TimeFormat.HOUR24SeON -> "24-Hour (With Seconds)"
    TimeFormat.HOUR12SeOFF -> "12-Hour"
    TimeFormat.HOUR24SeOFF -> "24-Hour"
}

fun DateFormat.toPattern(): String = when(this) {
    // Numeric month
    DateFormat.DayNMonthYear          -> "dd/MM/yyyy"
    DateFormat.NMonthDayYear          -> "MM/dd/yyyy"
    DateFormat.YearNMonthDay          -> "yyyy/MM/dd"
    DateFormat.SWeekDayNMonthYear     -> "E dd/MM/yyyy"
    DateFormat.SWeekNMonthDayYear     -> "E MM/dd/yyyy"
    DateFormat.SWeekYearNMonthDay     -> "E yyyy/MM/dd"
    DateFormat.WeekDayNMonthYear      -> "EEEE dd/MM/yyyy"
    DateFormat.WeekNMonthDayYear      -> "EEEE MM/dd/yyyy"
    DateFormat.WeekYearNMonthDay      -> "EEEE yyyy/MM/dd"

    // Short month name
    DateFormat.DaySMonthYear          -> "dd MMM yyyy"
    DateFormat.SMonthDayYear          -> "MMM dd yyyy"
    DateFormat.YearSMonthDay          -> "yyyy MMM dd"
    DateFormat.SWeekDaySMonthYear     -> "E dd MMM yyyy"
    DateFormat.SWeekSMonthDayYear     -> "E MMM dd yyyy"
    DateFormat.SWeekYearSMonthDay     -> "E yyyy MMM dd"
    DateFormat.WeekDaySMonthYear      -> "EEEE dd MMM yyyy"
    DateFormat.WeekSMonthDayYear      -> "EEEE MMM dd yyyy"
    DateFormat.WeekYearSMonthDay      -> "EEEE yyyy MMM dd"

    // Full month name
    DateFormat.DayFMonthYear          -> "dd MMMM yyyy"
    DateFormat.FMonthDayYear          -> "MMMM dd yyyy"
    DateFormat.YearFMonthDay          -> "yyyy MMMM dd"
    DateFormat.SWeekDayFMonthYear     -> "E dd MMMM yyyy"
    DateFormat.SWeekFMonthDayYear     -> "E MMMM dd yyyy"
    DateFormat.SWeekYearFMonthDay     -> "E yyyy MMMM dd"
    DateFormat.WeekDayFMonthYear      -> "EEEE dd MMMM yyyy"
    DateFormat.WeekFMonthDayYear      -> "EEEE MMMM dd yyyy"
    DateFormat.WeekYearFMonthDay      -> "EEEE yyyy MMMM dd"
}

fun String.toDateFormat(): DateFormat = when (this) {
    // Numeric month
    "dd/MM/yyyy"      -> DateFormat.DayNMonthYear
    "MM/dd/yyyy"      -> DateFormat.NMonthDayYear
    "yyyy/MM/dd"      -> DateFormat.YearNMonthDay
    "E dd/MM/yyyy"    -> DateFormat.SWeekDayNMonthYear
    "E MM/dd/yyyy"    -> DateFormat.SWeekNMonthDayYear
    "E yyyy/MM/dd"    -> DateFormat.SWeekYearNMonthDay
    "EEEE dd/MM/yyyy" -> DateFormat.WeekDayNMonthYear
    "EEEE MM/dd/yyyy" -> DateFormat.WeekNMonthDayYear
    "EEEE yyyy/MM/dd" -> DateFormat.WeekYearNMonthDay

    // Short month name
    "dd/MMM/yyyy"     -> DateFormat.DaySMonthYear
    "MMM/dd/yyyy"     -> DateFormat.SMonthDayYear
    "yyyy/MMM/dd"     -> DateFormat.YearSMonthDay
    "E dd/MMM/yyyy"   -> DateFormat.SWeekDaySMonthYear
    "E MMM/dd/yyyy"   -> DateFormat.SWeekSMonthDayYear
    "E yyyy/MMM/dd"   -> DateFormat.SWeekYearSMonthDay
    "EEEE dd/MMM/yyyy"-> DateFormat.WeekDaySMonthYear
    "EEEE MMM/dd/yyyy"-> DateFormat.WeekSMonthDayYear
    "EEEE yyyy/MMM/dd"-> DateFormat.WeekYearSMonthDay

    // Full month name
    "dd/MMMM/yyyy"     -> DateFormat.DayFMonthYear
    "MMMM/dd/yyyy"     -> DateFormat.FMonthDayYear
    "yyyy/MMMM/dd"     -> DateFormat.YearFMonthDay
    "E dd/MMMM/yyyy"   -> DateFormat.SWeekDayFMonthYear
    "E MMMM/dd/yyyy"   -> DateFormat.SWeekFMonthDayYear
    "E yyyy/MMMM/dd"   -> DateFormat.SWeekYearFMonthDay
    "EEEE dd/MMMM/yyyy"-> DateFormat.WeekDayFMonthYear
    "EEEE MMMM/dd/yyyy"-> DateFormat.WeekFMonthDayYear
    "EEEE yyyy/MMMM/dd"-> DateFormat.WeekYearFMonthDay
    else         -> DateFormat.DayNMonthYear // fallback default
}

fun DateFormat.toLabel(): String = when(this) {
    // Numeric month
    DateFormat.DayNMonthYear          -> "Eg 01/01/2026"
    DateFormat.NMonthDayYear          -> "Eg 01/01/2026"
    DateFormat.YearNMonthDay          -> "Eg 2026/01/01"
    DateFormat.SWeekDayNMonthYear     -> "Eg Fri 01/01/2026"
    DateFormat.SWeekNMonthDayYear     -> "Eg Fri 01/01/2026"
    DateFormat.SWeekYearNMonthDay     -> "Eg Fri 2026/01/01"
    DateFormat.WeekDayNMonthYear      -> "Eg Friday 01/01/2026"
    DateFormat.WeekNMonthDayYear      -> "Eg Friday 01/01/2026"
    DateFormat.WeekYearNMonthDay      -> "Eg Friday 2026/01/01"

    // Short month name
    DateFormat.DaySMonthYear          -> "Eg 01 Jan 2026"
    DateFormat.SMonthDayYear          -> "Eg Jan 01 2026"
    DateFormat.YearSMonthDay          -> "Eg 2026 Jan 01"
    DateFormat.SWeekDaySMonthYear     -> "Eg Fri 01 Jan 2026"
    DateFormat.SWeekSMonthDayYear     -> "Eg Fri Jan 01 2026"
    DateFormat.SWeekYearSMonthDay     -> "Eg Fri 2026 Jan 01"
    DateFormat.WeekDaySMonthYear      -> "Eg Friday 01 Jan 2026"
    DateFormat.WeekSMonthDayYear      -> "Eg Friday Jan 01 2026"
    DateFormat.WeekYearSMonthDay      -> "Eg Friday 2026 Jan 01"

    // Full month name
    DateFormat.DayFMonthYear          -> "Eg 01 January 2026"
    DateFormat.FMonthDayYear          -> "Eg January 01 2026"
    DateFormat.YearFMonthDay          -> "Eg 2026 January 01"
    DateFormat.SWeekDayFMonthYear     -> "Eg Fri 01 January 2026"
    DateFormat.SWeekFMonthDayYear     -> "Eg Fri January 01 2026"
    DateFormat.SWeekYearFMonthDay     -> "Eg Fri 2026 January 01"
    DateFormat.WeekDayFMonthYear      -> "Eg Friday 01 January 2026"
    DateFormat.WeekFMonthDayYear      -> "Eg Friday January 01 2026"
    DateFormat.WeekYearFMonthDay      -> "Eg Friday 2026 January 01"
}


//val fullFormat = "$dateFormat $timeFormat"



object GlobalSettings {
    var timeFormat by mutableStateOf(TimeFormat.HOUR24SeOFF.toPattern())
        private set
    var dateFormat by mutableStateOf(DateFormat.DayNMonthYear.toPattern())
        private set
    var dateTimeFormat by mutableStateOf("${DateFormat.DayNMonthYear.toPattern()} ${TimeFormat.HOUR24SeOFF.toPattern()}")
        private set

    fun setDateTimeFormat() {
        dateTimeFormat = "$dateFormat $timeFormat"
    }

    fun setTimeFormat(givenTimeFormat: TimeFormat) {
        timeFormat = givenTimeFormat.toPattern()
    }

    fun setDateFormat(givenDateFormat: DateFormat) {
        dateFormat = givenDateFormat.toPattern()
    }
}