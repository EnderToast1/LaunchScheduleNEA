package com.niaw.launchnea.ui.Screens.VehiclesScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.navigation.ViewModels.LaunchesViewModel
import com.niaw.launchnea.navigation.ViewModels.RocketViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxes

@Composable
fun ReusesScreen(searchText: String,scheduleDao: ScheduleDao ,onClick: (Reuse) -> Unit) {
    val viewModel= remember{ ReuseViewModel(scheduleDao)}
    val reuse by viewModel.reuseList.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Show list of launches
        ListOfBoxes(reuse, listState,onClick)
        SliderPlatform(reuse, listState)
    }

}
