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
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.ViewModels.LaunchesViewModel
import com.niaw.launchnea.navigation.ViewModels.RocketViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxes

@Composable
fun RocketsScreen(searchText: String,scheduleDao: ScheduleDao ,onClick: (Rocket) -> Unit) {
    val viewModel= remember{ RocketViewModel(scheduleDao)}
    val rockets by viewModel.rocket.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Show list of launches
        ListOfBoxes(rockets, listState,onClick)
        SliderPlatform(rockets, listState)
    }

}
