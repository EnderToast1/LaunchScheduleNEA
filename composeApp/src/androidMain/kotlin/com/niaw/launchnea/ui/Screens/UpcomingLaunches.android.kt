package com.niaw.launchnea.ui.Screens

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel
import com.niaw.launchnea.navigation.ViewModels.UpcomingLaunchViewModel
import com.niaw.launchnea.ui.UiElements.LaunchesList

@Composable
actual fun UpcomingLaunchesPlatform(scheduleDao: ScheduleDao) {
    val viewModel = remember { UpcomingLaunchViewModel(scheduleDao) }
    val upcomingLaunches by viewModel.launches.collectAsState()
    val listState = rememberLazyListState()

    LaunchesList(upcomingLaunches,listState)

}