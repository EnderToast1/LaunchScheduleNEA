package com.niaw.launchnea.ui.Screens.HomeScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.ViewModels.HomeViewModel
import com.niaw.launchnea.navigation.ViewModels.ReuseViewModel
import com.niaw.launchnea.ui.Screens.IndividualScreens.IndividualLaunchScreen
import com.niaw.launchnea.ui.Screens.IndividualScreens.LaunchDisplay
import com.niaw.launchnea.ui.Screens.IndividualScreens.LaunchHeading
import com.niaw.launchnea.ui.Screens.IndividualScreens.LaunchesSection

@Composable
fun HomeScreen(scheduleDao: ScheduleDao) {
    println("Click on Home bottom navigation.")
    val scrollState = rememberScrollState()
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected launch
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }
    val viewModel= remember{ HomeViewModel(scheduleDao) }
    val staredNextLaunches by viewModel.StaredupcomingLaunches.collectAsState()
    val staredPastLaunches by viewModel.StaredpastLaunches.collectAsState()
    val nextLaunches by viewModel.upcomingLaunches.collectAsState()
    val pastLaunches by viewModel.pastLaunches.collectAsState()

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

    else{
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Stared Launches", style = MaterialTheme.typography.titleLarge,color = MaterialTheme.colorScheme.secondary)
        HomeLaunchesSection(staredNextLaunches,staredPastLaunches){onClick-> selectedLaunchID = onClick}
        Spacer(Modifier.height(20.dp))
        Text(text = "All Launches", style = MaterialTheme.typography.titleLarge,color = MaterialTheme.colorScheme.secondary)
        HomeLaunchesSection(nextLaunches,pastLaunches){onClick-> selectedLaunchID = onClick}




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
        val count = if (expanded) 10 else 3
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
        val count = if (expanded) 10 else 1
        LaunchDisplay(count, pastLaunches, onClickList = onClick)
    }
}