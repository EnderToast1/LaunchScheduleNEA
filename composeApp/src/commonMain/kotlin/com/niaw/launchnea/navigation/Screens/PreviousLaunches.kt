package com.niaw.launchnea.navigation.Screens


import androidx.compose.runtime.Composable
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.getPlatform

@Composable
fun PreviousLaunches(scheduleDao: ScheduleDao) {
    PreviousLaunchesPlatform(scheduleDao)
}
@Composable
expect fun PreviousLaunchesPlatform(scheduleDao: ScheduleDao)
