package com.niaw.launchnea

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.StarredLaunch
import com.niaw.launchnea.networking.updateLaunchData

@Composable
fun App(scheduleDao: ScheduleDao) {
    MaterialTheme{
        LaunchedEffect(Unit){
            updateLaunchData(scheduleDao)
            println("Done updateLaunchData")
            val userInput = "9642cfef-4687-495a-af2e-2ac81fa1e12a"
            val star= StarredLaunch(userInput)
            if (scheduleDao.launchExistsById(userInput)) {
                scheduleDao.starLaunch(star)

            }
            else{
                println("not found")
            }
            scheduleDao.getStarredLaunches().collect {launch ->
                println(launch)
            }
        }

    }
}