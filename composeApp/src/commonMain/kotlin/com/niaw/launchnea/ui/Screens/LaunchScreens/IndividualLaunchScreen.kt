package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.abbreviate
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.setBackHandler
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.search_book
import org.jetbrains.compose.resources.painterResource
import java.time.LocalTime

@Composable
fun IndividualLaunchScreen(launch: Launch,  onBack: () -> Unit) {
        LaunchedEffect(launch) {
                setBackHandler(enabled = true) {
                        onBack()
                }
        }

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp)) {
                HeaderRow(launch, onBack)//Back and Agency boxes

                MissionMainBox() //TODO Mission and Time Box
                Spacer(Modifier.height(10.dp))
                LiveStreamBox()
                Spacer(Modifier.height(15.dp))
                MissionDetails(launch)

                //TODO Rocket Section
                //TODO Reuse
                //TODO Pad
                //TODO Stats
        }
}

@Composable
fun HeaderRow(launch: Launch,onBack: () -> Unit){
    val agencyName=abbreviate(launch.agencyName?: "Unknown")

    val calculatedWeight = remember(agencyName) {
            val minWeight = 1f
            val maxWeight = 2f
            val normalized = (agencyName.length / 50f).coerceIn(0f, 1f) // 50 chars = max
            minWeight + normalized * (maxWeight - minWeight)
    }
    val animatedWeight by animateFloatAsState(calculatedWeight)


    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

            Box(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp, 12.dp))
                            .background(Color.Green)
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .clickable { onBack() }
            ) {
                    Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Back", style = MaterialTheme.typography.bodyMedium)
                    }
            }

            Spacer(modifier = Modifier.weight(1.5f))

            Box(
                    modifier = Modifier
                            .weight(animatedWeight)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp, 12.dp))
                            .background(Color.Red)
                            .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                    Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                    ) {
                            Icon(Icons.Filled.Business, contentDescription = "Agency")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(agencyName, style = MaterialTheme.typography.bodyMedium)
                    }
            }
    }
}


@Composable
fun MissionMainBox(){
        var selectedZone by remember {mutableStateOf(LiftoffTimeZone.PAD_LOCAL)}

        Box(modifier = Modifier.background(Color.Yellow).height(300.dp).fillMaxWidth().padding(10.dp)) {
                Column(Modifier.fillMaxWidth()) {
                        Text(
                                "Mission Name",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        TimeDropDown(selected = selectedZone, onSelected = { selectedZone = it }, modifier = Modifier.fillMaxWidth())

                        Spacer(Modifier.height(15.dp))
                        LaunchWindowTimeline(LocalTime.of(3, 23), LocalTime.of(3, 30), LocalTime.of(4, 32))
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropDown(
        selected: LiftoffTimeZone,
        onSelected: (LiftoffTimeZone) -> Unit,
        modifier: Modifier = Modifier
        ) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier.clip(RoundedCornerShape(12.dp))) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                                .clickable { expanded = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                        Text(
                                text = "Lift-off Time (${selected.label})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.width(0.dp))

                        Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Select time zone"
                        )
                }

                DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                ) {
                        LiftoffTimeZone.entries.forEach { zone ->
                                DropdownMenuItem(
                                        text = { Text(zone.label) },
                                        onClick = {
                                                onSelected(zone)
                                                expanded = false
                                        }
                                )
                        }
                }
        }
}


enum class LiftoffTimeZone(val label: String) {
        PAD_LOCAL("Pad local"),
        UTC("UTC"),
        DEVICE_LOCAL("Device local")
}

@Composable
fun LiveStreamBox(){//TODO add click functionality
        Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(percent = 50)) // pill shape
                        .background(Color(0xFFC40000))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        Text(
                                text = "Watch Livestream",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge
                        )

                        Icon(
                                imageVector = Icons.Outlined.Link,
                                contentDescription = "Livestream link",
                                tint = Color.White
                        )
                }
        }
}

@Composable
fun MissionDetails(launch:Launch){
        MissionDetailsHeader()
        Spacer(Modifier.height(10.dp))
        MissionDetailsBody(launch)

}

@Composable
fun MissionDetailsHeader(){
        val textStyle = MaterialTheme.typography.headlineLarge
        val iconSize = with(LocalDensity.current) {
                textStyle.fontSize.toDp()
        }

        Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Icon(
                        painter = painterResource(Res.drawable.search_book),
                        contentDescription = "Mission Details",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(iconSize)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                        text = "Mission Details",
                        style = textStyle,
                        color = MaterialTheme.colorScheme.primary
                )
        }
}

@Composable
fun MissionDetailsBody(launch:Launch){
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer)) {
                Box(Modifier.padding(10.dp)) {
                        launch.missionName?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(Modifier.padding(10.dp)) {
                        launch.description?.let {
                                Text(
                                        it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                }
        }

}


@Composable
fun LaunchWindowTimeline(timeOpen: LocalTime, timeLaunch: LocalTime, timeClose: LocalTime) {
        val openSeconds = timeOpen.toSecondOfDay()
        val launchSeconds = timeLaunch.toSecondOfDay()
        val closeSeconds = timeClose.toSecondOfDay()

        val progress = ((launchSeconds - openSeconds).toFloat() /
                (closeSeconds - openSeconds))
                .coerceIn(0f, 1f)

        Column {
                BoxWithConstraints(
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp) // space for label + timeline
                ) {
                        val dotSize = 10.dp
                        val usableWidth = maxWidth - dotSize
                        val markerX = usableWidth * progress

                        // === Timeline base ===
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                        ) {
                                TimelineDot(color = Color.Gray, Modifier)

                                Divider(
                                        modifier = Modifier
                                                .weight(1f)
                                                .height(4.dp),
                                        color = Color.DarkGray
                                )

                                Divider(
                                        modifier = Modifier
                                                .weight(1f)
                                                .height(4.dp),
                                        color = Color.DarkGray
                                )

                                TimelineDot(color = Color.Gray, Modifier)
                        }

                        // === Planned Liftoff label (above line) ===
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                        .offset(x = markerX-4.5.dp,y= (-5).dp)
                                        .align(Alignment.TopStart)
                        ) {
                                Icon(
                                        imageVector = Icons.Filled.Rocket,
                                        contentDescription = "Planned Launch"
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                        text = "Planned Liftoff",
                                        style = MaterialTheme.typography.labelSmall
                                )
                        }

                        // === Green launch dot (on the line) ===
                        TimelineDot(
                                color = Color.Green,
                                modifier = Modifier
                                        .offset(x = markerX)
                                        .align(Alignment.CenterStart)
                        )
                }

                Spacer(Modifier.height(4.dp))

                // === Window labels ===
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        Text(
                                text = "Window Open\n$timeOpen",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Start
                        )

                        Text(
                                text = "Window Close\n$timeClose",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.End
                        )
                }
        }
}

@Composable
fun TimelineDot(color: Color,modifier: Modifier) {
        Box(
                modifier = modifier.size(16.dp).background(color, CircleShape)
        )
}

