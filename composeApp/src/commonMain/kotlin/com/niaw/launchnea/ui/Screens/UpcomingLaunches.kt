package com.niaw.launchnea.ui.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.niaw.launchnea.database.ScheduleDao

@Composable
fun UpcomingLaunches(scheduleDao: ScheduleDao) {
    UpcomingLaunchesPlatform(scheduleDao)
}
@Composable
expect fun UpcomingLaunchesPlatform(scheduleDao: ScheduleDao)
