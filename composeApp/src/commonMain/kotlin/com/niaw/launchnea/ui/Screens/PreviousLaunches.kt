package com.niaw.launchnea.ui.Screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.ui.UiElements.LaunchBox

@Composable
fun PreviousLaunches(scheduleDao: ScheduleDao) {
    PreviousLaunchesPlatform(scheduleDao)
}
@Composable
expect fun PreviousLaunchesPlatform(scheduleDao: ScheduleDao)




