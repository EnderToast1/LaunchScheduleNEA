package com.niaw.launchnea.utils

import com.niaw.launchnea.database.entities.Launch
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun enableLaunchReminder(launch: Launch) {
    val minutesBefore=10//TODO add settings to change this value

    val reminderTime  = launch.launchTimeUTC!!.minusMinutes(minutesBefore.toLong())


    if (reminderTime.isAfter(ZonedDateTime.now(ZoneOffset.UTC))) {
        println("Reminder set for: $reminderTime ")
        println("Reminder set for: ${launch.launchTimeUTC} ")

        NotificationScheduler.scheduleNotification(
            reminderTime.toInstant().toEpochMilli() ,
            "Upcoming Launch",
            "${launch.missionName} launches in $minutesBefore minutes",
            (launch.launchID+minutesBefore).hashCode()
        )

        NotificationScheduler.scheduleNotification(
            reminderTime.toInstant().toEpochMilli() ,
            "Launch",
            "${launch.missionName} is launching now",
            (launch.launchID+0).hashCode()
        )
    }
}


