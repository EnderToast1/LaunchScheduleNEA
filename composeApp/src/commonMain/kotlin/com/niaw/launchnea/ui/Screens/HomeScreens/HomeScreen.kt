package com.niaw.launchnea.ui.Screens.HomeScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.ViewModels.HomeViewModel
import com.niaw.launchnea.navigation.ViewModels.OnThisDayViewModel
import com.niaw.launchnea.ui.Screens.IndividualScreens.IndividualLaunchScreen
import com.niaw.launchnea.ui.Screens.IndividualScreens.LaunchDisplay
import com.niaw.launchnea.ui.Screens.IndividualScreens.LaunchHeading
import com.niaw.launchnea.ui.UiElements.IconTextRow
import com.niaw.launchnea.ui.UiElements.LogoTextRow
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.logo
import launchnea.composeapp.generated.resources.logo2
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(scheduleDao: ScheduleDao) {

    val scrollState = rememberScrollState()
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected launch
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }
    val viewModel= remember{ HomeViewModel(scheduleDao) }
    val staredNextLaunches by viewModel.StaredupcomingLaunches.collectAsState()
    val staredPastLaunches by viewModel.StaredpastLaunches.collectAsState()
    val nextLaunches by viewModel.upcomingLaunches.collectAsState()
    val pastLaunches by viewModel.pastLaunches.collectAsState()



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(0.dp,0.dp,20.dp,20.dp))
                    .background(Color(0xFF00205F))
                    .padding(horizontal = 4.dp, vertical = 4.dp), Alignment.Center
            ) {
                LogoTextRow("HorizonPoint Space","Logo", MaterialTheme.typography.displaySmall, painterResource(Res.drawable.logo2), Modifier, Color.White)
            }
            Spacer(Modifier.height(5.dp))

            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 4.dp, vertical = 4.dp), Alignment.Center
            ) {
                Text(
                    text = "Starred Launches",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            HomeLaunchesSection(
                staredNextLaunches,
                staredPastLaunches
            ) { onClick -> selectedLaunchID = onClick }
            Spacer(Modifier.height(10.dp))

            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 4.dp, vertical = 4.dp), Alignment.Center
            ) {
                Text(
                    text = "All Launches",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            HomeLaunchesSection(nextLaunches, pastLaunches) { onClick ->
                selectedLaunchID = onClick
            }
            Spacer(Modifier.height(10.dp))

            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 4.dp, vertical = 4.dp), Alignment.Center
            ) {
                Text(
                    text = "On this Day",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            OnThisDay(scheduleDao) { onClick -> selectedLaunchID = onClick }
        }

        if (selectedLaunchID != null) {
            LaunchedEffect(selectedLaunchID) {
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
    }

}

@Composable
fun HomeLaunchesSection(nextLaunches: List<Launch>, pastLaunches: List<Launch>, onClick: (String) -> Unit) {
    var isNextExpanded by remember { mutableStateOf(false) }
    var isPastExpanded by remember { mutableStateOf(false) }

    // Next Launches
    LaunchHeading("Next Launch", isExpanded = isNextExpanded) {
        isNextExpanded = !isNextExpanded
    }

    // This animates the container height and content appearance
    AnimatedContent(
        targetState = isNextExpanded,
        transitionSpec = {fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))}
    ) { expanded  ->
        val count = if (expanded) 10 else 2
        LaunchDisplay(count, nextLaunches, onClickList = onClick)
    }

    Spacer(Modifier.height(7.5.dp))

    // Past Launches
    LaunchHeading("Past Launch", isExpanded = isPastExpanded) {
        isPastExpanded = !isPastExpanded
    }

    AnimatedContent(
        targetState = isPastExpanded,
        transitionSpec = {fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))}
    ) { expanded  ->
        val count = if (expanded) 10 else 2
        LaunchDisplay(count, pastLaunches, onClickList = onClick)
    }
}

@Composable
fun OnThisDay(scheduleDao: ScheduleDao, onClick: (String) -> Unit){
    val viewModel = remember{OnThisDayViewModel(scheduleDao)}

    val launches by viewModel.launches.collectAsState()

    LaunchDisplay(0, launches, onClickList = onClick)
}