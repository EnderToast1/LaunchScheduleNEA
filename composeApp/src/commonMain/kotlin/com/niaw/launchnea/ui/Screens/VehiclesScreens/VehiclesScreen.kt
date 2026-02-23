package com.niaw.launchnea.ui.Screens.VehiclesScreens

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
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.TabItem
import kotlinx.coroutines.launch


@Composable
fun VehiclesScreen(scheduleDao: ScheduleDao) {
    println("Click on Vehicles bottom navigation.")
    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) } // track selected Rocket
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) } // track selected Reuse


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
            if (selectedRocket != null) {
                println("Clicked on ${selectedRocket!!.name}")
                IndividualRocketScreen(selectedRocket!!) {
                    println("Backed out of ${selectedRocket!!.name}")
                    selectedRocket = null
                }

            } else if (selectedReuse != null) {
                println("Clicked on ${selectedReuse!!.name}")
                IndividualReuseScreen(selectedReuse!!) {
                    println("Backed out of ${selectedReuse!!.name}")
                    selectedReuse = null
                }

        }else {
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

                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Search") },
                        singleLine = true
                    )

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
                                println("Click on Rocket tab row")
                                RocketsScreen(searchText,scheduleDao){ rocket ->selectedRocket = rocket}
                            }

                            1 -> {
                                println("Click on Reuse tab row")
                                ReusesScreen()
                            }

                        }
                    }
                }
            }
        }
    }
}