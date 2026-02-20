package com.niaw.launchnea.ui.Screens


import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.ui.UiElements.LaunchesList
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel

@Composable
actual fun PreviousLaunchesPlatform(scheduleDao: ScheduleDao) {
    val viewModel = remember { PreviousLaunchViewModel(scheduleDao) }
    val pastLaunches by viewModel.launches.collectAsState()
    val listState = rememberLazyListState()

    LaunchesList(pastLaunches,listState)
}