package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.niaw.launchnea.navigation.ViewModels.LaunchesViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxesLaunches

@Composable
fun ListLaunchesScreen(viewModel: LaunchesViewModel,listState: LazyListState, searchText: String,onClick: (String) -> Unit) {

    val launchesFromViewModel by viewModel.launches.collectAsState()

    var launches by remember {
        mutableStateOf(launchesFromViewModel)
    }

    LaunchedEffect(launchesFromViewModel) {
        if (launchesFromViewModel.isNotEmpty()) {
            launches = launchesFromViewModel
        }
    }


    // Only jump to top when the query changes — not when returning from a detail screen
    var previousSearch by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        if (previousSearch != null && previousSearch != searchText) {
            listState.scrollToItem(0)
        }
        previousSearch = searchText
    }

    Box(modifier = Modifier.fillMaxSize()) {

            // Show list of launches
        ListOfBoxesLaunches(null,launches, listState,onClick)
        SliderPlatform(launches, listState)
    }

}