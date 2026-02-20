package com.niaw.launchnea.ui.UiElements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.ReuseType
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse

val cardShape = RoundedCornerShape(
    topStart = 20.dp,
    bottomStart = 0.dp,
    topEnd = 0.dp,
    bottomEnd = 20.dp
)
val boxModifier = Modifier
    .fillMaxWidth()
    .height(140.dp)
    .border(
        width = 1.dp,
        color = Color(0xB3787878),
        shape = cardShape
    )
    .clip(cardShape)
    .background(Color(0xFFFFFFFF))
    .padding(16.dp)


@Composable
fun LaunchBox(launch: Launch) {
    if (launch.missionName!=null) {
        Box(
            modifier = boxModifier
        ) {
            //Content
            LaunchBoxContent(launch)
        }
    }
}

@Composable
fun LaunchBoxContent(launch: Launch) {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        LaunchImage(launch)

        LaunchDetails(launch)
    }
}

@Composable
private fun LaunchImage(launch: Launch) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .background(Color(0xFF1C1C22)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,//TODO add image URL display
            contentDescription = null
        )
        //TODO add licence and credit
    }
}

@Composable
private fun LaunchDetails(launch: Launch) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        LaunchChipsRow(launch)

        launch.missionName?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        Text("${launch.agencyName} | ${launch.rocketName}", style = MaterialTheme.typography.bodySmall)

        launch.padName?.let { Text(it, style = MaterialTheme.typography.bodySmall) }

        Text(launch.launchTimeUTC.toString(), style = MaterialTheme.typography.bodySmall)//TODO add change to time zone and a custom toString
    }
}

@Composable
private fun LaunchChipsRow(launch: Launch) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
       launch.reusableParts.forEach { reusablePart ->
            println("ReusablePart: $reusablePart")
            LaunchChip(reusablePart)
        }

    }
}

@Composable
private fun LaunchChip(reusablePart: Reusable ) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFD0D0D0))
            .padding(horizontal = 5.dp, vertical = 1.dp)
    ) {
        reusablePart.landingLocationAbbr?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
    }
}