package com.niaw.launchnea

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.niaw.launchnea.database.getDatabase

fun main() = application {
    val dao = remember { getDatabase().ScheduleDao() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "launchnea",
    ) {
        App(dao)
    }
}