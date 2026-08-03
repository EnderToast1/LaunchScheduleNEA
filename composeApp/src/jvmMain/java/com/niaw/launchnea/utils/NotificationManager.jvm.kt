package com.niaw.launchnea.utils

import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage

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