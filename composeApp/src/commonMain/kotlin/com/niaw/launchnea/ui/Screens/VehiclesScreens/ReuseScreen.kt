package com.niaw.launchnea.ui.Screens.VehiclesScreens

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
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.ReuseViewModel
import com.niaw.launchnea.ui.SliderPlatform
import com.niaw.launchnea.ui.UiElements.ListOfBoxesReuse

@Composable
fun ReusesScreen(
    searchText: String,
    scheduleDao: ScheduleDao,
    showOnlyActive: Boolean,
    viewModel: ReuseViewModel,
    listState: LazyListState,
    onClick: (String) -> Unit,
) {
    val reuseFromViewModel by viewModel.reuse.collectAsState()
    var reuse by remember { mutableStateOf(reuseFromViewModel) }

    LaunchedEffect(reuseFromViewModel) {
        if (reuseFromViewModel.isNotEmpty()) {
            reuse = reuseFromViewModel
        } else if (searchText.isNotBlank() || showOnlyActive) {
            reuse = reuseFromViewModel
        }
    }

    var previousSearch by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(searchText) {
        viewModel.updateSearch(searchText)
        if (previousSearch != null && previousSearch != searchText) {
            listState.scrollToItem(0)
        }
        previousSearch = searchText
    }

    var previousShowOnlyActive by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(showOnlyActive) {
        viewModel.updateOnlyShowActive(showOnlyActive)
        if (previousShowOnlyActive != null && previousShowOnlyActive != showOnlyActive) {
            listState.scrollToItem(0)
        }
        previousShowOnlyActive = showOnlyActive
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ListOfBoxesReuse(scheduleDao, reuse, listState, onClick)
        SliderPlatform(reuse, listState)
    }
}
