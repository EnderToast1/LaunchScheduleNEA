package com.niaw.launchnea.Settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.niaw.launchnea.utils.DateFormat
import com.niaw.launchnea.utils.TimeFormat

object GlobalSettings {
    var timeFormat by mutableStateOf(TimeFormat.HOUR24SeOFF.toPattern())
        private set
    var dateFormat by mutableStateOf(DateFormat.DayNMonthYear.toPattern())
        private set
    var dateTimeFormat by mutableStateOf("${DateFormat.DayNMonthYear.toPattern()} ${TimeFormat.HOUR24SeOFF.toPattern()}")
        private set
    var successColour by mutableStateOf(Color.Green)
    var partialFailureColour by mutableStateOf(Color.Yellow)
    var failureColour by mutableStateOf(Color.Red)
    var inFlightColour by mutableStateOf(Color.Blue)
    var holdColour by mutableStateOf(Color(0xFFFF6600))

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