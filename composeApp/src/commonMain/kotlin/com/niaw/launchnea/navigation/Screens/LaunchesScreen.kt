package com.niaw.launchnea.navigation.Screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel
import com.niaw.launchnea.ui.TabItem
import kotlinx.coroutines.launch



@Composable
fun LaunchesScreen(scheduleDao: ScheduleDao) {
    val tabItems = listOf(
        TabItem("Upcoming"),
        TabItem("Previous"),
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
                                val targetPage = if (offset > threshold) page + 1 else if (offset < -threshold) page - 1 else page
                                pagerState.animateScrollToPage(targetPage.coerceIn(0, tabItems.size - 1))
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                pagerState.scrollBy(-dragAmount)
                            }
                        }
                    )
                },
                key = { it } // optional but can help preserve state
            ) { page ->
                when (page) {
                    0 -> UpcomingLaunches()
                    1 -> PreviousLaunches(scheduleDao)
                }
            }
        }
    }
}