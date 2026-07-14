package com.niaw.launchnea.ui.Screens.IndividualScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.Settings.GlobalSettings.dateTimeFormat
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.navigation.TabItem
import com.niaw.launchnea.navigation.ViewModels.AgencyViewModel
import com.niaw.launchnea.navigation.ViewModels.HomeViewModel
import com.niaw.launchnea.navigation.ViewModels.ListBoxRocketInfoViewModel
import com.niaw.launchnea.navigation.ViewModels.PreviousLaunchViewModel
import com.niaw.launchnea.navigation.ViewModels.ReuseBoxInfo
import com.niaw.launchnea.navigation.ViewModels.UpcomingLaunchViewModel
import com.niaw.launchnea.navigation.setBackHandler
import com.niaw.launchnea.ui.Screens.HomeScreens.HomeLaunchesSection
import com.niaw.launchnea.ui.Screens.LaunchScreens.ListLaunchesScreen
import com.niaw.launchnea.ui.UiElements.Chip
import com.niaw.launchnea.ui.UiElements.EmptyListDisplay
import com.niaw.launchnea.ui.UiElements.IconTextRow
import com.niaw.launchnea.ui.UiElements.ListBox
import com.niaw.launchnea.ui.UiElements.ListOfBoxesReuse
import com.niaw.launchnea.ui.UiElements.ListOfBoxesRocket
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun IndividualAgencyScreen(scheduleDao: ScheduleDao, agency: Agency, onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    var selectedRocketID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Rocket
    var selectedReuseID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Reuse
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Launch
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) }
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }

    setBackHandler(enabled = selectedLaunchID != null || selectedReuseID != null || selectedRocketID != null) {
        println("Internal Back: Closing sub-screen")
        selectedLaunchID = null
        selectedReuseID = null
        selectedRocketID = null
        selectedLaunch = null
        selectedReuse = null
        selectedRocket = null
    }

    val tabItems = listOf(
        TabItem("Rockets"),
        TabItem("Reusable Vehicles"),
        TabItem("Launches"),
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount={tabItems.size}
    )
    val scope = rememberCoroutineScope()



    // THIS HANDLES MAIN SCREEN BACK PRESS
    // Active only when the main list is visible. It calls the parent's back function.
    setBackHandler(enabled = selectedLaunchID == null && selectedReuseID == null && selectedRocketID == null) {
        println("Main Back: Exiting IndividualReuseScreen")
        onBack()
    }

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
            IndividualReuseScreen(scheduleDao, reuse) {
                println("Backed out of ${reuse.name}")
                selectedReuse = null
                selectedReuseID = null
            }
        }
    }
    else if (selectedLaunchID != null) {
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
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(scrollState)) {
            agency.name?.let { TopLogo(agency.logoURL, it) }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(20.dp)
                ).padding(16.dp)
            ) {
                agency.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    agency.leader?.let {
                        Text(
                            "│$it",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Text(
                        "│${agency.leaderType.displayName}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VertIconLink(Icons.Filled.Language, "Website", agency.websiteURL)
                VertIconLink(Icons.Filled.Language, "Wikipedia", agency.wikiURL)
                VertIconLink(Icons.Filled.Language, "X Account", agency.socialsURL)
                VertIconLink(Icons.Filled.Language, "YouTube", agency.youtubeURL)
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                                scope.launch { pagerState.scrollBy(-dragAmount) }
                            }
                        )
                    },
                    key = { it }
                ) { page ->

                    when (page) {
                        0 -> {
                            AgencyRockets(agency,scheduleDao){onClick2 -> selectedRocketID = onClick2}
                        }

                        1 -> {
                            AgencyReuse(agency,scheduleDao){onClick2 -> selectedReuseID = onClick2}
                        }

                        2 -> {
                            AgencyLaunches(agency,scheduleDao){onClick2 -> selectedLaunchID = onClick2}
                        }

                    }
                }
            }


        }

    }

}

@Composable
fun TopLogo(url: String?, titleText: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Color.Transparent))
        Box(
            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
        ) {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ImageNotSupported,
                            contentDescription = "Image not found",
                            tint = Color.LightGray,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            )

            // gradient overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                    ))
            )
        }
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Brush.verticalGradient(
            0f to MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
            1f to MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
        ))
        )
            // title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                            0.5f to MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f),
                        ),
                        RoundedCornerShape(0.dp,0.dp,40.dp,40.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )


            }

    }
}


@Composable
fun VertIconLink(image:ImageVector,title:String,link:String?) {

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = rememberVectorPainter(image),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (link != null) {
            Text(
                text = buildAnnotatedString {
                    pushLink(LinkAnnotation.Url(link))
                    append(title)
                    pop()
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        } else {
            Text(
                "No Link Found",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}


@Composable
fun AgencyRockets(agency: Agency,scheduleDao: ScheduleDao,onClick2: (String) -> Unit) {
    val viewModel= remember{ AgencyViewModel(scheduleDao, agency.agencyID) }
    val rockets by viewModel.rockets.collectAsState()
    if (rockets.isEmpty()){
        EmptyListDisplay()
    }
        Column(

            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            rockets.forEach{ item ->
                val itemText = item.listBoxText()

                val viewModel = remember(item.rocketID) {
                    ListBoxRocketInfoViewModel(scheduleDao, item.rocketID)
                }

                val flights by viewModel.flights.collectAsState()
                itemText.thirdRow="Flights: $flights"

                val lastFlight:Launch? by viewModel.lastFlight.collectAsState()
                if (lastFlight != null) {
                    val duration = Duration.between(lastFlight?.launchTimeUTC, ZonedDateTime.now())

                    val days = duration.toMillis() / (1000.0 * 60 * 60 * 24)
                    val absDays = kotlin.math.abs(days)
                    val formattedDays = "%.1f".format(absDays)

                    val unit = if (absDays == 1.0) "day" else "days"

                    itemText.fourthRow =
                        "Last Flight: ${lastFlight?.launchTimeUTC?.format(DateTimeFormatter.ofPattern(dateTimeFormat))} ($formattedDays $unit ago)"
                }




                ListBox(itemText, onClick2)
            }

    }
}

@Composable
fun AgencyReuse(agency: Agency,scheduleDao: ScheduleDao,onClick2: (String) -> Unit) {
    val viewModel= remember{ AgencyViewModel(scheduleDao, agency.agencyID) }
    val reuses by viewModel.reuse.collectAsState()
    if (reuses.isEmpty()){
        EmptyListDisplay()
    }

    Column(

        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
       reuses.forEach{ item ->


                val itemText = item.listBoxText()

                val viewModel = remember(item.reuseID) {
                    ReuseBoxInfo(scheduleDao, item.reuseID)
                }

                val flights by viewModel.flights.collectAsState()
                val lastFlight:Launch? by viewModel.lastFlight.collectAsState()
                if (lastFlight != null) {
                    val duration = Duration.between(lastFlight?.launchTimeUTC, ZonedDateTime.now())

                    val days = duration.toMillis() / (1000.0 * 60 * 60 * 24)
                    val absDays = kotlin.math.abs(days)
                    val formattedDays = "%.1f".format(absDays)

                    val unit = if (absDays == 1.0) "day" else "days"

                    itemText.fourthRow =
                        "Last Flight: ${lastFlight?.launchTimeUTC?.format(DateTimeFormatter.ofPattern(dateTimeFormat))} ($formattedDays $unit ago)"
                }

                itemText.chips= listOf(Chip("$flights"))

                ListBox(itemText, onClick2)
            }
        }


}


@Composable
fun AgencyLaunches(agency: Agency,scheduleDao: ScheduleDao,onClick2: (String) -> Unit) {
    val viewModel= remember{ AgencyViewModel(scheduleDao, agency.agencyID) }
    val nextLaunches by viewModel.upcomingLaunches.collectAsState()
    val pastLaunches by viewModel.pastLaunches.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        HomeLaunchesSection(nextLaunches, pastLaunches, onClick2)
    }

}