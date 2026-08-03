package com.niaw.launchnea.ui.Screens.IndividualScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.navigation.ViewModels.RocketLaunchesViewModel
import com.niaw.launchnea.navigation.setBackHandler
import com.niaw.launchnea.ui.UiElements.IconTextRow
import com.niaw.launchnea.ui.UiElements.ListBox
import com.niaw.launchnea.utils.Status

@Composable
fun IndividualRocketScreen(scheduleDao: ScheduleDao, rocket: Rocket, onBackRocket: () -> Unit) {
    val scrollState = rememberScrollState()

    val viewModel = remember(rocket.rocketID) {
        RocketLaunchesViewModel(scheduleDao, rocket.rocketID)
    }

    val nextLaunches by viewModel.upcomingLaunches.collectAsState()
    val pastLaunches by viewModel.pastLaunches.collectAsState()

    val successes by viewModel.successes.collectAsState()
    val failures by viewModel.failures.collectAsState()
    val partialFailures by viewModel.partialFailures.collectAsState()
    val launches by viewModel.launches.collectAsState()



    var selectedReuseID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Rocket
    var selectedLaunchID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Launch
    var selectedAgencyID by rememberSaveable { mutableStateOf<String?>(null) } // track selected Agency
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) }
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) }
    var selectedAgency by remember { mutableStateOf<Agency?>(null) }

    var agencyName by rememberSaveable { mutableStateOf<String?>(null) }
    var agencyLogo by rememberSaveable { mutableStateOf<String?>(null) }
    var rocketVersion by remember { mutableStateOf<RocketVersion?>(null) }
    var successStreak by rememberSaveable { mutableIntStateOf(0) }
    successStreak=calculateSuccessStreak(launches)

    // THIS HANDLES SUB-SCREEN BACK PRESS
    // Active only when you are looking at an Agency, Rocket, or Launch
    setBackHandler(enabled = selectedAgencyID != null || selectedLaunchID != null || selectedReuseID != null) {
        println("Internal Back: Closing sub-screen")
        selectedAgencyID = null
        selectedLaunchID = null
        selectedReuseID = null
        selectedAgency = null
        selectedLaunch = null
        selectedReuse = null
    }

    // THIS HANDLES MAIN SCREEN BACK PRESS
    // Active only when the main list is visible. It calls the parent's back function.
    setBackHandler(enabled = selectedAgencyID == null && selectedLaunchID == null && selectedReuseID == null) {
        onBackRocket()
    }

    //Used to get information using the DAO
    LaunchedEffect(rocket) {
        agencyName = rocket.agencyID?.let { scheduleDao.getAgencyById(it) }?.name
        agencyLogo = rocket.agencyID?.let { scheduleDao.getAgencyById(it) }?.logoURL
    }

    //Navigates to different windows
    if (selectedReuseID != null) {
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
            IndividualAgencyScreen(scheduleDao,agency) {
                println("Backed out of ${agency.name}")
                selectedAgency = null
                selectedAgencyID = null
            }
        }
    }

    else {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            rocket.name?.let { TopImage(rocket.imageURL,it, agencyName) }
            Column(Modifier.fillMaxWidth()) {
                RocketStats(rocket,successes,failures,partialFailures,successStreak)
                Spacer(Modifier.height(10.dp))
                AgencyBox(rocket.agencyID, agencyName) { agencyID -> selectedAgencyID = agencyID }
                Spacer(Modifier.height(10.dp))
                ConfigHeading()
                Spacer(Modifier.height(5.dp))
                VersionsList(rocket,scheduleDao)
            }
                Spacer(Modifier.height(10.dp))
                LaunchesSection(nextLaunches,pastLaunches){onClick-> selectedLaunchID = onClick}

            }
        }

}


@Composable
fun RocketStats(rocket: Rocket,successes: Int, failures: Int, partialFailures: Int, successStreak: Int) {
    val missions=successes+failures+partialFailures
    val successRate = if (missions > 0) {((successes + 0.5 * partialFailures) / missions) * 100} else {0.0}

    //TODO maybe change to just success not 0.5 of partials



    Column(modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,15.dp,15.dp)).fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Missions:\n $missions", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Success Rate:\n $successRate%", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Success:\n $successes", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Failure:\n $failures", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Success Streak:\n $successStreak", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("PartialFailures:\n $partialFailures", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ConfigHeading() {
    IconTextRow("Configuration","Cog Wheel",MaterialTheme.typography.headlineMedium,rememberVectorPainter(Icons.Filled.Settings),Modifier.padding(bottom = 8.dp),MaterialTheme.colorScheme.onBackground)

}

@Composable
fun VersionsList(rocket: Rocket,scheduleDao:ScheduleDao) {
    val viewModel = remember(rocket.rocketID) {
        RocketLaunchesViewModel(scheduleDao, rocket.rocketID)
    }

    val rocketVersions by viewModel.versions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // spacing between boxes
    ) {
        rocketVersions.forEach{ version  ->
            VersionBox(version )
        }
    }
}

@Composable
fun VersionBox(rocketVersion: RocketVersion) {
    ListBox(rocketVersion.listBoxText()){onClick -> println("Clicked on $rocketVersion")}
}

private fun calculateSuccessStreak(launches: List<Launch>): Int {
    var streak = 0

    for (launch in launches) {
        if (launch.success == Status.SUCCESS) {
            streak++
        } else {
            break
        }
    }

    return streak
}