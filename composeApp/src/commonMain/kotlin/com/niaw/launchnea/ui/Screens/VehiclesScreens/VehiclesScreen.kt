package com.niaw.launchnea.ui.Screens.VehiclesScreens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.TabItem
import com.niaw.launchnea.navigation.ViewModels.ReuseViewModel
import com.niaw.launchnea.navigation.ViewModels.RocketViewModel
import com.niaw.launchnea.ui.Screens.IndividualScreens.IndividualReuseScreen
import com.niaw.launchnea.ui.Screens.IndividualScreens.IndividualRocketScreen
import kotlinx.coroutines.launch


@Composable
fun VehiclesScreen(scheduleDao: ScheduleDao) {

    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedRocketID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Rocket
    var selectedReuseID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Reuse
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) }
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    var showOnlyActive by remember { mutableStateOf(false) }

    val listStateRockets = rememberLazyListState()
    val listStateReuse = rememberLazyListState()
    val rocketViewModel = remember { RocketViewModel(scheduleDao) }
    val reuseViewModel = remember { ReuseViewModel(scheduleDao) }

    val tabItems = listOf(
        TabItem("Rockets"),
        TabItem("Reusable Vehicles"),
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount={tabItems.size}
    )

    val scope = rememberCoroutineScope()


    Surface(
        modifier= Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){

        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedRocketID != null) {
                LaunchedEffect(selectedRocketID){
                    selectedRocket = scheduleDao.getRocketById(id = selectedRocketID!!)
                }
                selectedRocket?.let { rocket ->
                    println("Clicked on ${rocket.name},  \n$rocket")
                    IndividualRocketScreen(scheduleDao,rocket) {
                        println("Backed out of ${rocket.name}")
                        selectedRocket = null
                        selectedRocketID = null
                    }
                }
            }
            else if (selectedReuseID != null) {
                LaunchedEffect(selectedReuseID){
                    selectedReuse = scheduleDao.getReuseById(id = selectedReuseID!!)
                }
                selectedReuse?.let { reuse ->
                    println("Clicked on ${reuse.name},  \n$reuse")
                    IndividualReuseScreen(scheduleDao,reuse) {
                        println("Backed out of ${reuse.name}")
                        selectedReuse = null
                        selectedReuseID = null
                    }
                }
            }
            else {
                // Show the tab row, search, and list only when no Rocket/Reuse is selected
                Column(modifier = Modifier.fillMaxSize()) {
                    SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
                        tabItems.forEachIndexed { index, item ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = { Text(item.title) }
                            )
                        }
                    }

                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {

                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = { Text("Search") },
                            singleLine = true
                        )

                        SettingsMenu(enabled = showOnlyActive,onEnabledChange = { showOnlyActive = it })
                    }
                    // HorizontalPager handles swipe gestures on desktop and Android
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize().pointerInput(pagerState) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    scope.launch {
                                        // Snap to nearest page
                                        val page = pagerState.currentPage
                                        val offset = pagerState.currentPageOffsetFraction
                                        val threshold = 0.1f
                                        val targetPage = if (offset > threshold) page + 1
                                        else if (offset < -threshold) page - 1 else page
                                        pagerState.animateScrollToPage(targetPage.coerceIn(0, tabItems.size - 1))
                                    }
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    scope.launch {pagerState.scrollBy(-dragAmount)}
                                }
                            )
                        },
                        key = { it } // optional but can help preserve state
                    ) { page ->
                        when (page) {

                            0 -> {
                                RocketsScreen(
                                    searchText = searchText,
                                    scheduleDao = scheduleDao,
                                    showOnlyActive = showOnlyActive,
                                    viewModel = rocketViewModel,
                                    listState = listStateRockets,
                                ) { rocketId -> selectedRocketID = rocketId }
                            }

                            1 -> {
                                ReusesScreen(
                                    searchText = searchText,
                                    scheduleDao = scheduleDao,
                                    showOnlyActive = showOnlyActive,
                                    viewModel = reuseViewModel,
                                    listState = listStateReuse,
                                ) { reuseId -> selectedReuseID = reuseId }
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SettingsMenu(enabled: Boolean, onEnabledChange: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(8.dp)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Settings",
                modifier = Modifier.size(40.dp)

            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ToggleMenuItem(text = "Show Only Active",checked = enabled,onCheckedChange = onEnabledChange)
        }
    }
}



@Composable
fun ToggleMenuItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color.White,
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedTrackColor = Color(0xFFF44336)
                    )
                )
            }
        },
        onClick = { onCheckedChange(!checked) }
    )
}
