package com.niaw.launchnea.ui.Screens.VehiclesScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.navigation.ViewModels.ReuseViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxesReuse

@Composable
fun ReusesScreen(searchText: String,scheduleDao: ScheduleDao ,onClick: (String) -> Unit) {
    val viewModel= remember{ ReuseViewModel(scheduleDao) }
    val reuse by viewModel.reuse.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Show list of launches
        ListOfBoxesReuse(scheduleDao,reuse, listState,onClick)
        SliderPlatform(reuse, listState)
    }

}
