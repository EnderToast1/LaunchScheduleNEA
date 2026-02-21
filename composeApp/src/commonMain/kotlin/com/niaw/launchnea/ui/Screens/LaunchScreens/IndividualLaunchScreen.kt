package com.niaw.launchnea.ui.Screens.LaunchScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.navigation.setBackHandler

@Composable
fun IndividualLaunchScreen(launch: Launch,  onBack: () -> Unit) {
        LaunchedEffect(launch) {
                setBackHandler(enabled = true) {
                        onBack()
                }
        }

        Column(modifier = Modifier.fillMaxSize()) {
                Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                launch.missionName?.let { Text(it) }
        }

}