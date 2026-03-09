package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.TabItem
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel
import com.niaw.launchnea.navigation.ViewModels.UpcomingLaunchViewModel
import com.niaw.launchnea.ui.Screens.IndividualScreens.IndividualLaunchScreen
import kotlinx.coroutines.launch



@Composable
fun LaunchesScreen(scheduleDao: ScheduleDao) {
    println("Click on Launches bottom navigation.")
    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected launch
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }

    val tabItems = listOf(
        TabItem("Upcoming"),
        TabItem("Previous"),
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount={tabItems.size}
    )
    val scope = rememberCoroutineScope()


    if (selectedLaunchID != null) {
                LaunchedEffect(selectedLaunchID){
                selectedLaunch = scheduleDao.getLaunchById(id = selectedLaunchID!!)
                }
                selectedLaunch?.let { launch ->
                    println("Clicked on ${launch.missionName},  \n$launch")
                    IndividualLaunchScreen(scheduleDao,launch) {
                        println("Backed out of ${launch.missionName}")
                        selectedLaunch = null
                        selectedLaunchID = null
                    }
                }
            }
    else {
                Surface(
                    modifier= Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                // Show the tab row, search, and list only when no launch is selected
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

                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Search missions") },
                        singleLine = true
                    )
                    LaunchedEffect(pagerState.currentPage) {
                        when (pagerState.currentPage) {
                            0 -> println("Click on Upcoming tab row")
                            1 -> println("Click on Previous tab row")
                        }
                    }

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
                                        scope.launch { pagerState.scrollBy(-dragAmount)}
                                    }
                                )
                            },
                        key = { it }
                    ) { page ->

                        when (page) {
                            0 -> {
                                ListLaunchesScreen(remember { UpcomingLaunchViewModel(scheduleDao) }, searchText) { launchId ->selectedLaunchID = launchId }
                            }

                            1 -> {
                                ListLaunchesScreen(remember { PreviousLaunchViewModel(scheduleDao) },searchText) { launchId -> selectedLaunchID = launchId }
                            }

                        }
                    }
                }
            }
        }

}