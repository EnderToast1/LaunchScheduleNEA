package com.niaw.launchnea.ui.Screens.IndividualScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.*
import com.niaw.launchnea.navigation.ViewModels.ReuseLaunchesViewModel
import com.niaw.launchnea.navigation.setBackHandler
import com.niaw.launchnea.ui.UiElements.ListBox
import kotlin.math.round

@Composable
fun IndividualReuseScreen(scheduleDao: ScheduleDao,reuse: Reuse,  onBackReuse: () -> Unit) {
    val viewModel = remember(reuse.reuseID) {
        ReuseLaunchesViewModel(scheduleDao, reuse.reuseID)
    }
    val nextLaunches by viewModel.upcomingLaunches.collectAsState()
    val pastLaunches by viewModel.pastLaunches.collectAsState()
    val reuses by viewModel.reuses.collectAsState()
    val averageReuseTime by viewModel.averageReuseTime.collectAsState()
    val fastestReuseTime by viewModel.fastestReuseTime.collectAsState()

    val scrollState = rememberScrollState()


    var selectedRocketID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Rocket
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Launch
    var selectedAgencyID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Agency
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }
    var selectedAgency by remember { mutableStateOf<Agency?>(null) }

    var agencyName by rememberSaveable { mutableStateOf<String?>(null) }
    var agencyLogo by rememberSaveable { mutableStateOf<String?>(null) }
    var rocketVersion by remember { mutableStateOf<RocketVersion?>(null) }

    // THIS HANDLES SUB-SCREEN BACK PRESS
    // Active only when you are looking at an Agency, Rocket, or Launch
    setBackHandler(enabled = selectedAgencyID != null || selectedLaunchID != null || selectedRocketID != null) {
        println("Internal Back: Closing sub-screen")
        selectedAgencyID = null
        selectedLaunchID = null
        selectedRocketID = null
        selectedAgency = null
        selectedLaunch = null
        selectedRocket = null
    }

    // THIS HANDLES MAIN SCREEN BACK PRESS
    // Active only when the main list is visible. It calls the parent's back function.
    setBackHandler(enabled = selectedAgencyID == null && selectedLaunchID == null && selectedRocketID == null) {
        println("Main Back: Exiting IndividualReuseScreen")
        onBackReuse()
    }

    //Used to get information using the DAO
    LaunchedEffect(reuse) {
        agencyName = reuse.agencyID?.let { scheduleDao.getAgencyById(it) }?.name
        agencyLogo = reuse.agencyID?.let { scheduleDao.getAgencyById(it) }?.logoURL
        rocketVersion = scheduleDao.getRocketVersionById(reuse.versionID)
    }

    //Navigates to different windows
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
    else if (selectedLaunchID != null) {
        LaunchedEffect(selectedLaunchID){
            selectedLaunch = scheduleDao.getLaunchById(id = selectedLaunchID!!)
        }
        selectedLaunch?.let { launch ->
            println("Clicked on ${launch.missionName},  \n$launch")
            IndividualLaunchScreen(scheduleDao, launch) {
                println("Backed out of ${launch.missionName}")
                selectedLaunch = null
                selectedLaunchID = null
            }
        }
    }
    else if (selectedAgencyID != null) {
        LaunchedEffect(selectedAgencyID){
            selectedAgency = scheduleDao.getAgencyById(id = selectedAgencyID!!)
        }
        selectedAgency?.let { agency ->
            println("Clicked on ${agency.name},  \n$agency")
            IndividualAgencyScreen(agency) {
                println("Backed out of ${agency.name}")
                selectedAgency = null
                selectedAgencyID = null
            }
        }
    }

    else{
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(scrollState)) {

            //Image and Stats
            TopImage(reuse.imageURL,reuse.name!!, agencyName)
            ReuseStatsBox(reuse, reuses, averageReuseTime, fastestReuseTime)

            //Configuration Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                ConfigHeading()//Heading with icon text
                rocketVersion?.let {VersionBox(it)}// The actual config Version box
            }


            Spacer(modifier = Modifier.size(10.dp))
            AgencyBox(reuse.agencyID,agencyName,agencyLogo) { agencyID ->selectedAgencyID=agencyID}
            Spacer(modifier = Modifier.size(10.dp))
            LaunchesSection(nextLaunches,pastLaunches){onClick-> selectedLaunchID = onClick}


        }
    }

}


@Composable
fun ReuseStatsBox(reuse: Reuse,reuses:Int,averageReuseTime:Long?,fastestReuseTime:Long?){

    val status=reuse.status
    val avgDays = remember(averageReuseTime) {
        round((averageReuseTime?.let { millis ->
            millis / (1000.0 * 60 * 60 * 24)
        } ?: 1.0)*100 )/100
    }
    val fastestDays = remember(fastestReuseTime) {
        round((fastestReuseTime?.let { it / (1000.0 * 60 * 60 * 24) } ?: 1.0)*100)/100
    }


    Column(modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,20.dp,20.dp)).fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(20.dp,7.5.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Row 1
        DataRow(
            leftLabel = "Status", leftValue = status.toString(),
            rightLabel = "Flights", rightValue = "$reuses"
        )
        // Row 2
        DataRow(
            leftLabel = "Average reuse time", leftValue = if (avgDays > 0) "$avgDays days" else "N/A",
            rightLabel = "Fastest reuse time", rightValue = if (fastestDays > 0) "$fastestDays days" else "N/A",
        )


    }
}

@Composable
fun DataRow(leftLabel: String, leftValue: String, rightLabel: String, rightValue: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(//Label
                text = leftLabel,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(//Data
                text = leftValue,
                modifier = Modifier.padding(start = 5.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }

        // Right Column
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(//Label
                text = rightLabel,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(//Data
                text = rightValue,
                modifier = Modifier.padding(end = 5.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Composable
fun TopImage(url: String?, titleText: String, secondText:String?) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Rocket Image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
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
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.75f)
                    )
                )
        )

        // title
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            secondText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun LaunchesSection(nextLaunches: List<Launch>, pastLaunches: List<Launch>, onClick: (String) -> Unit) {
    var showNext by remember { mutableIntStateOf(1) }
    var showPast by remember { mutableIntStateOf(3) }

    // Next Launches
    LaunchHeading("Next Launch", isExpanded = showNext == 0) {
        showNext = if (showNext == 0) 1 else 0
    }

    // This animates the container height and content appearance
    AnimatedContent(
        targetState = showNext,
        transitionSpec = {fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))}
    ) { targetShowNext ->
        LaunchDisplay(targetShowNext, nextLaunches, onClickList = onClick)
    }

    Spacer(Modifier.height(7.5.dp))

    // Past Launches
    LaunchHeading("Past Launch", isExpanded = showPast == 0) {
        showPast = if (showPast == 0) 3 else 0
    }

    AnimatedContent(
        targetState = showPast,
        transitionSpec = {fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))}
    ) { targetShowPast ->
        LaunchDisplay(targetShowPast, pastLaunches, onClickList = onClick)
    }
}


@Composable
fun LaunchHeading(title: String,isExpanded: Boolean, onClick: () -> Unit) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
            // Rounded at top, Flat at bottom
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            color = MaterialTheme.colorScheme.secondaryContainer, // The lavender/purple color
            tonalElevation = 2.dp // Adds subtle M3 depth
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side: Icon and Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
                // Right side: "See More" link
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "See Less" else "See More",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow",
                        modifier = Modifier.size(20.dp).rotate(rotation),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

@Composable
fun LaunchDisplay(defaultNumberOfLaunch: Int = 0,launches: List<Launch>, onClickList: (String) -> Unit) {
    var takeCount =defaultNumberOfLaunch
    if (defaultNumberOfLaunch == 0) { takeCount=launches.size }

    Column(modifier = Modifier.fillMaxWidth()/*.padding(16.dp,0.dp)*/,  verticalArrangement = Arrangement.spacedBy(5.dp)) {
        launches.take(takeCount).forEach { launch ->
            ListBox(launch.listBoxText(), onClick = onClickList)
        }
    }
}

