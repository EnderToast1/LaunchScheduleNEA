package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.ViewModels.LaunchesViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxesLaunches

@Composable
fun ListLaunchesScreen(viewModel: LaunchesViewModel, searchText: String,onClick: (String) -> Unit) {

    val launches by viewModel.launches.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {

            // Show list of launches
        ListOfBoxesLaunches(null,launches, listState,onClick)
        println("number of launches: ${launches.size }")
        SliderPlatform(launches, listState)
    }

}