package com.niaw.launchnea.utils

import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.ui.Screens.IndividualScreens.LiftoffTimeZone
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class LaunchTimes(
    val liftoff: LocalDateTime?,
    val windowOpen: LocalDateTime?,
    val windowClose: LocalDateTime?
)
data class LaunchTimesFormated(
    val liftoffDate: String,
    val liftoffTime: String,
    val liftoff:LocalDateTime,
    val windowOpen: LocalDateTime,
    val windowClose: LocalDateTime
)

//TODO make function
fun getLaunchTimesInTimeZone(launch: Launch, selectedZone: LiftoffTimeZone):LaunchTimes {

    if (selectedZone == LiftoffTimeZone.PAD_LOCAL) {
        val zoneId = try {
            ZoneId.of(launch.padTimeZone)
        } catch (e: Exception) {
            ZoneId.of("UTC")
        }


        return LaunchTimes(
            liftoff = launch.launchTimeUTC?.withZoneSameInstant(zoneId)?.toLocalDateTime(),
            windowOpen = launch.windowOpen?.withZoneSameInstant(zoneId)?.toLocalDateTime(),
            windowClose = launch.windowClose?.withZoneSameInstant(zoneId)?.toLocalDateTime()
        )

    }else if (selectedZone == LiftoffTimeZone.DEVICE_LOCAL) {
        val zoneId = ZoneId.systemDefault()

        return LaunchTimes(
            liftoff = launch.launchTimeUTC?.withZoneSameInstant(zoneId)?.toLocalDateTime(),
            windowOpen = launch.windowOpen?.withZoneSameInstant(zoneId)?.toLocalDateTime(),
            windowClose = launch.windowClose?.withZoneSameInstant(zoneId)?.toLocalDateTime()
        )

    }else{
        return LaunchTimes(
            liftoff = launch.launchTimeUTC?.toLocalDateTime(),
            windowOpen = launch.windowOpen?.toLocalDateTime(),
            windowClose = launch.windowClose?.toLocalDateTime()
    )

    }
}


fun getLaunchTimesAsNeededFormat(launch: Launch,dateFormatter: DateTimeFormatter,timeFormater: DateTimeFormatter, selectedZone:LiftoffTimeZone): LaunchTimesFormated {
        val times = getLaunchTimesInTimeZone(launch,selectedZone)

        return LaunchTimesFormated(
            liftoffDate = times.liftoff?.format(dateFormatter) ?:"Error" ,
            liftoffTime = times.liftoff?.format(timeFormater) ?:"Error" ,
            liftoff = times.liftoff ?: LocalDateTime.now(),
            windowOpen = times.windowOpen ?: LocalDateTime.now(),
            windowClose = times.windowClose ?: LocalDateTime.now()
        )
    }
