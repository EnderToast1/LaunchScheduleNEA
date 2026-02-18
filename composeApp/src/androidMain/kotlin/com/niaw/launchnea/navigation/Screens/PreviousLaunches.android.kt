package com.niaw.launchnea.navigation.Screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel

@Composable
actual fun PreviousLaunchesPlatform(scheduleDao: ScheduleDao) {
    val viewModel = remember { PreviousLaunchViewModel(scheduleDao) }
    val launches by viewModel.launches.collectAsState()

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize()
    ) {
        items(launches, key = { it.launchID }) { launch ->
            launch.missionName?.let {
                Text(text = it, modifier = Modifier.padding(16.dp))
            }
        }
    }
}