package com.niaw.launchnea.utils

import com.niaw.launchnea.database.entities.Launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class LaunchTimes(
    val liftoff: LocalDateTime,
    val windowOpen: LocalDateTime,
    val windowClose: LocalDateTime
)
data class LaunchTimesFormated(
    val liftoffDate: String,
    val liftoffTime: String,
    val liftoff:LocalDateTime,
    val windowOpen: LocalDateTime,
    val windowClose: LocalDateTime
)

//TODO make function
fun getLaunchTimesInTimeZone(launch: Launch):LaunchTimes {
    return LaunchTimes(
        liftoff = launch.launchTimeUTC?.toLocalDateTime() ?: LocalDateTime.now() ,
        windowOpen = launch.windowOpen?.toLocalDateTime() ?: LocalDateTime.now(),
        windowClose = launch.windowClose?.toLocalDateTime() ?:LocalDateTime.now()
    )
}


fun getLaunchTimesAsNeededFormat(launch: Launch,dateFormatter: DateTimeFormatter,timeFormater: DateTimeFormatter): LaunchTimesFormated {
        val times = getLaunchTimesInTimeZone(launch)

        return LaunchTimesFormated(
            liftoffDate = times.liftoff.format(dateFormatter),
            liftoffTime = times.liftoff.format(timeFormater),
            liftoff = times.liftoff,
            windowOpen = times.windowOpen,
            windowClose = times.windowClose
        )
    }
