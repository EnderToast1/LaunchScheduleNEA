package com.niaw.launchnea

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niaw.launchnea.database.getDatabase
import com.niaw.launchnea.utils.AppSettings
import com.russhwolf.settings.PreferencesSettings


fun main() {

    val dao = getDatabase().ScheduleDao()
    val prefs = java.util.prefs.Preferences.userRoot().node("com.your.app.settings")
    val settings = PreferencesSettings(prefs)
    val themeSettings = AppSettings(settings)
    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Horizon Spaceflight",
        ) {
            App(
                scheduleDao = remember { dao },
                appSettings = remember { themeSettings }
            )
        }
    }
}
