package com.niaw.launchnea.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

actual object NotificationScheduler {

    actual fun scheduleNotification(
        timeMillis: Long,
        title: String,
        message: String,
        requestCode: Int,
    ) {
        CoroutineScope(Dispatchers.Default).launch {

            val delayTime =
                timeMillis - System.currentTimeMillis()

            delay(delayTime.milliseconds)

            NotificationManager.showNotification(
                title,
                message
            )
        }
    }
}