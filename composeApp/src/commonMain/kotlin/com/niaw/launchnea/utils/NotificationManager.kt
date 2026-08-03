package com.niaw.launchnea.utils

expect object NotificationManager {
    fun showNotification(
        title: String,
        message: String
    )
}

expect object NotificationScheduler {
    fun scheduleNotification(
        timeMillis: Long,
        title: String,
        message: String,
        requestCode: Int,
    )
    fun cancelNotification(
        launchId: String
    )
}