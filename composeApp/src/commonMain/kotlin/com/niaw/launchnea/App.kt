package com.niaw.launchnea

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.networking.updateLaunchData

@Composable
fun App(scheduleDao: ScheduleDao) {
    MaterialTheme{
        LaunchedEffect(Unit){
            updateLaunchData()
        }

    }
}