package com.niaw.launchnea.utils

import com.niaw.launchnea.database.entities.Launch
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun enableLaunchReminder(launch: Launch) {
    val minutesBefore=10//TODO add settings to change this value
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    val launchTime = launch.launchTimeUTC!!

    val reminderTime = launchTime.minusMinutes(minutesBefore.toLong())

    if (reminderTime.isAfter(now)) {
        println("Reminder set for: $reminderTime")
        println("Launch time: $launchTime")

        NotificationScheduler.scheduleNotification(
            reminderTime.toInstant().toEpochMilli(),
            "Upcoming Launch",
            "${launch.missionName} launches in $minutesBefore minutes",
            (launch.launchID + minutesBefore).hashCode()
        )
    }

// Schedule launch notification independently
    if (launchTime.isAfter(now)) {
        NotificationScheduler.scheduleNotification(
            launchTime.toInstant().toEpochMilli(),
            "Launch",
            "${launch.missionName} is launching now",
            launch.launchID.hashCode()
        )
    }
}


