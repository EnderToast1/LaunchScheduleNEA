package com.niaw.launchnea.ui.Screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel
import com.niaw.launchnea.ui.UiElements.LaunchesList


@Composable
actual fun PreviousLaunchesPlatform(scheduleDao: ScheduleDao) {
    val viewModel = remember { PreviousLaunchViewModel(scheduleDao) }
    val pastLaunches by viewModel.launches.collectAsState()
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        // LazyColumn handles scrolling
        LaunchesList(pastLaunches,listState)

        // Scrollbar just mirrors LazyColumn's state
        if (pastLaunches.isNotEmpty()) {
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }

}