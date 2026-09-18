package com.niaw.launchnea.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage
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

    actual fun cancelNotification(launchId: String) {
    }
}


actual object NotificationManager {

    private var trayIcon: TrayIcon? = null

    init {
        if (SystemTray.isSupported()) {
            val tray = SystemTray.getSystemTray()

            val image = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)

            trayIcon = TrayIcon(image, "Launch App")
            trayIcon!!.isImageAutoSize = true

            tray.add(trayIcon)
        }
    }

    actual fun showNotification(
        title: String,
        message: String
    ) {
        trayIcon?.displayMessage(
            title,
            message,
            TrayIcon.MessageType.INFO
        )
    }
}