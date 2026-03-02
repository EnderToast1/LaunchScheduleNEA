package com.niaw.launchnea

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niaw.launchnea.database.getDatabase


fun main() {

    val dao = getDatabase().ScheduleDao()

    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Launch TimeTable",
        ) {
            App(remember{dao})
        }
    }
}