package com.niaw.launchnea.ui.Screens.IndividualScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.navigation.setBackHandler
import com.niaw.launchnea.ui.UiElements.IconTextRow
import com.niaw.launchnea.ui.UiElements.ImagePlaceholder
import com.niaw.launchnea.ui.UiElements.ListBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun IndividualRocketScreen(scheduleDao: ScheduleDao, rocket: Rocket, onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    var selectedLaunch by remember { mutableStateOf<Launch?>(null) } // track selected Launch
    var selectedReuse by remember { mutableStateOf<Reuse?>(null) } // track selected Reuse
    var selectedAgency by remember { mutableStateOf<Agency?>(null) } // track selected Agency

    var agencyName by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(rocket) {
        agencyName= rocket.agencyID?.let { scheduleDao.getAgencyById(it) }?.name

        setBackHandler(enabled = true) {
            onBack()
        }
    }
    if (selectedLaunch != null) {
        println("Clicked on ${selectedLaunch!!.missionName},  \n$selectedLaunch")
        IndividualLaunchScreen(scheduleDao,selectedLaunch!!) {
            println("Backed out of ${selectedLaunch!!.missionName}")
            selectedLaunch = null
        }
    }
    else if (selectedReuse != null) {
        println("Clicked on ${selectedReuse!!.name}\n $selectedReuse")
        IndividualReuseScreen(scheduleDao,selectedReuse!!) {
            println("Backed out of ${selectedReuse!!.name}")
            selectedReuse = null
        }
    }
    else if (selectedAgency != null) {
        println("Clicked on ${selectedAgency!!.name},  \n$selectedAgency")
        IndividualAgencyScreen(selectedAgency!!) {
            println("Backed out of${selectedAgency!!.name}")
            selectedAgency = null
        }
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            TopImage(rocket.imageURL!!,rocket.name!!, agencyName)
            Column(Modifier.fillMaxWidth()) {
                RocketStats(rocket)
                Spacer(Modifier.height(10.dp))
                AgencyBox(rocket.agencyID,agencyName) { agencyID ->}
                Spacer(Modifier.height(10.dp))
                ConfigHeading()
                Spacer(Modifier.height(5.dp))
                VersionsList(rocket)

            }
        }
    }
}


@Composable
fun RocketStats(rocket: Rocket) {

    val success = 5//TODO get real data
    val failure = 3
    val partialFailures = 2

    val missions=success+failure+partialFailures
    val successRate=((success+0.5*(partialFailures))/missions)*100  //TODO maybe change to just success not 0.5 of partials

    val successStreak = 3

    Column(modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,15.dp,15.dp)).fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Missions:\n $missions", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Success Rate:\n $successRate%", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Success:\n $success", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
            Text("Failure:\n $failure", style = MaterialTheme.typography.bodyMedium,modifier = Modifier.weight(1f))
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
fun VersionsList(rocket: Rocket) {
    val rocketVersions: MutableList<RocketVersion> =mutableListOf()//TODO get list
    rocketVersions.add(RocketVersion("A12","TEST 1"))
    rocketVersions.add(RocketVersion("A13","TEST 2"))

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
    ListBox(rocketVersion){}
}

