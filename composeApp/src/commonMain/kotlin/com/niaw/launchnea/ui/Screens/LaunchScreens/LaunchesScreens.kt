package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.niaw.launchnea.navigation.ViewModels.LaunchesViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.LaunchesList

@Composable
fun launchesScreen(viewModel: LaunchesViewModel, searchText: String
) {
    val launches by viewModel.launches.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LaunchesList(launches, listState)
        SliderPlatform(launches, listState)
    }
}