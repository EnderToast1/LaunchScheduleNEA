package com.niaw.launchnea.utils

import com.niaw.launchnea.Settings.GlobalSettings.dateFormat
import com.niaw.launchnea.Settings.GlobalSettings.dateTimeFormat
import com.niaw.launchnea.Settings.GlobalSettings.timeFormat
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.ui.Screens.IndividualScreens.LiftoffTimeZone
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields

fun getNetLaunchTime(
    launch: Launch,
    selectedZone: LiftoffTimeZone = LiftoffTimeZone.DEVICE_LOCAL,
): String {
    val launchTime = launch.launchTimeUTC ?: return "TBD"

    val zoneId = when (selectedZone) {
        LiftoffTimeZone.PAD_LOCAL -> try {
            ZoneId.of(launch.padTimeZone)
        } catch (_: Exception) {
            ZoneId.of("UTC")
        }
        LiftoffTimeZone.UTC -> ZoneId.of("UTC")
        LiftoffTimeZone.DEVICE_LOCAL -> ZoneId.systemDefault()
    }

    val local = launchTime.withZoneSameInstant(zoneId).toLocalDateTime()

    val dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
    val timeFormatter = DateTimeFormatter.ofPattern(timeFormat)
    val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat)
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    val netAbbrev = launch.precisionAbbrev ?: return local.format(dateTimeFormatter)

    return when (netAbbrev) {
        "SEC" -> local.format(dateTimeFormatter)
        "MIN" -> local.withSecond(0).withNano(0).format(dateTimeFormatter)
        "HR" -> {
            val hourRounded = local.withMinute(0).withSecond(0).withNano(0)
            "NET ${local.format(dateFormatter)} ${hourRounded.format(timeFormatter)}"
        }
        "AM" -> "NET morning of ${local.format(dateFormatter)}"
        "PM" -> "NET afternoon of ${local.format(dateFormatter)}"
        "DAY" -> "NET ${local.format(dateFormatter)}"
        "WK" -> {
            val week = local.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            val weekYear = local.get(IsoFields.WEEK_BASED_YEAR)
            "NET Week $week, $weekYear"
        }
        "M" -> "NET ${local.format(monthYearFormatter)}"
        "Q1", "Q2", "Q3", "Q4", "H1", "H2" -> "NET $netAbbrev ${local.year}"
        "Y" -> "NET ${local.year}"
        "FY" -> "NET Fiscal Year ${local.year}"
        "DEC" -> {
            val decadeStart = (local.year / 10) * 10
            "NET ${decadeStart}s"
        }
        else -> local.format(dateTimeFormatter)
    }
}
