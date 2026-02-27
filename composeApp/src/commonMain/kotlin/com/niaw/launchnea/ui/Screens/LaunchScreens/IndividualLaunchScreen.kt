package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.abbreviate
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.setBackHandler

@Composable
fun IndividualLaunchScreen(launch: Launch,  onBack: () -> Unit) {
        LaunchedEffect(launch) {
                setBackHandler(enabled = true) {
                        onBack()
                }
        }

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp)) {
                HeaderRow(launch, onBack)//Back and Agency boxes

                Box(Modifier.fillMaxWidth().height(100.dp).background(Color.Yellow)) //TODO Mission and Time Box
                //TODO LiveStream Box
                //TODO Mission details
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
fun MissionHeaderCard() {
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
        ) {
                Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                                text = "Mission Name",
                                style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                        text = "Lift-off Time (Time-Zone)",
                                        style = MaterialTheme.typography.labelMedium
                                )
                                Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                                text = "00:00",
                                style = MaterialTheme.typography.displayMedium
                        )

                        Spacer(Modifier.height(12.dp))

                        InfoRow(icon = Icons.Default.CalendarMonth, text = "DAY DATE")
                        InfoRow(icon = Icons.Default.Place, text = "Pad Name")

                        Spacer(Modifier.height(16.dp))

                        LaunchWindowTimeline()
                }
        }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
        ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(text, style = MaterialTheme.typography.bodyMedium)
        }
}

@Composable
fun LaunchWindowTimeline() {
        Column {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                ) {
                        TimelineDot(color = Color.Gray)

                        Divider(
                                modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp),
                                color = Color.DarkGray
                        )

                        TimelineDot(color = Color.Green)

                        Divider(
                                modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp),
                                color = Color.DarkGray
                        )

                        TimelineDot(color = Color.Gray)
                }

                Spacer(Modifier.height(4.dp))

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        Text("Window Open\n00:00", style = MaterialTheme.typography.labelSmall)
                        Text("Window Close\n00:00", style = MaterialTheme.typography.labelSmall)
                }
        }
}

@Composable
fun TimelineDot(color: Color) {
        Box(
                modifier = Modifier
                        .size(16.dp)
                        .background(color, CircleShape)
        )
}

@Composable
fun LivestreamButton() {
        Button(
                onClick = { /* open stream */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                shape = RoundedCornerShape(50)
        ) {
                Text("Watch Livestream")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Link, contentDescription = null)
        }
}

@Composable
fun MissionDetailsCard() {
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
        ) {
                Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                                text = "Mission Name",
                                style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                                text = "Details",
                                style = MaterialTheme.typography.bodyMedium
                        )
                }
        }
}