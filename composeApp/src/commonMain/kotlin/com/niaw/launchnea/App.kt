 package com.niaw.launchnea

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.ui.Screens.HomeScreens.HomeScreen
import com.niaw.launchnea.ui.Screens.LaunchScreens.LaunchesScreen
import com.niaw.launchnea.ui.Screens.SettingsScreen
import com.niaw.launchnea.ui.Screens.VehiclesScreens.VehiclesScreen
import com.niaw.launchnea.navigation.NavigationItem
import com.niaw.launchnea.Settings.AppSettings
import com.niaw.launchnea.utils.AppTheme
import com.niaw.launchnea.Settings.GlobalSettings
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.StarredLaunch
import com.niaw.launchnea.networking.requestNumber
import com.niaw.launchnea.networking.updateLaunchData
import com.niaw.launchnea.utils.NotificationManager
import com.niaw.launchnea.utils.NotificationScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.withContext
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.filledrocketh
import org.jetbrains.compose.resources.painterResource
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


 @Suppress("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App(scheduleDao: ScheduleDao,appSettings: AppSettings) {

    var currentRoute by rememberSaveable  { mutableStateOf("home") }
    var currentTheme by remember { mutableStateOf(appSettings.getTheme()) }
    var currentColourTheme by remember { mutableStateOf(appSettings.getColourTheme()) }
    LaunchedEffect(Unit) {
        GlobalSettings.setTimeFormat(appSettings.getTimeFormat())
        GlobalSettings.setDateFormat(appSettings.getDateFormat())
        GlobalSettings.setDateTimeFormat()
        GlobalSettings.successColour=(appSettings.getColourSuccess())
        GlobalSettings.partialFailureColour=(appSettings.getColourPartialFailure())
        GlobalSettings.failureColour=(appSettings.getColourFailure())
        GlobalSettings.inFlightColour=(appSettings.getColourInFlight())
        GlobalSettings.holdColour=(appSettings.getColourHold())
    }



    AppTheme(selectedMode = currentTheme, seedColourType = currentColourTheme ) {
        /*
        NotificationManager.showNotification(
            "Launch Reminder",
            "Launches in 15 minutes."
        )

        NotificationScheduler.scheduleNotification(
            ZonedDateTime.now().plusMinutes(1).toInstant().toEpochMilli(),
            "Launch Reminder",
            "Launches in 10 minutes",
            1000
        )
        */

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                try {
                    //println(InetAddress.getByName("ll.thespacedevs.com"))
                } catch (e: Exception) {
                    println(e)
                }

                //delay(1.seconds)
                //updateLaunchData(scheduleDao)
            }
        }


        val items = listOf<NavigationItem>(
            NavigationItem(
                title = "Home",
                route = "home",
                selectedIcon = {Icon(Icons.Filled.Home,"Home")},
                unselectedIcon = {Icon(Icons.Outlined.Home,"Home")},
            ),
            NavigationItem(
                title = "Launches",
                route = "launches",
                selectedIcon = {
                    Icon(
                        painterResource(Res.drawable.FilledRocket), "Rocket", modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current
                    )
                },
                unselectedIcon = {
                    Icon(
                        painterResource(Res.drawable.FilledRocket), "Rocket", modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current
                    )
                },
            ),
            NavigationItem(
                title = "Vehicles",
                route = "vehicles",
                selectedIcon = {Icon(painterResource(Res.drawable.filledrocketh),"Rocket", modifier = Modifier.size(24.dp),
                    tint = LocalContentColor.current)},
                unselectedIcon = {Icon(painterResource(Res.drawable.filledrocketh),"Rocket", modifier = Modifier.size(24.dp),
                    tint = LocalContentColor.current)},
            ),
            NavigationItem(
                title = "Settings",
                route = "settings",
                selectedIcon = {Icon(Icons.Filled.Settings,"Home")},
                unselectedIcon = {Icon(Icons.Outlined.Settings,"Home")},
            )

        )



        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Red
        ) {

            Scaffold(modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        items.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = { currentRoute = item.route},
                                label = {Text(text = item.title)},
                                icon = {if (currentRoute == item.route) {item.selectedIcon()} else {item.unselectedIcon()}}
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    when (currentRoute) {
                        "home"     -> HomeScreen(scheduleDao)
                        "launches" -> LaunchesScreen(scheduleDao)
                        "vehicles" -> VehiclesScreen(scheduleDao)
                        "settings" -> SettingsScreen(
                                currentTheme = currentTheme,
                                currentColourTheme = currentColourTheme,
                                appSettings = appSettings,
                                onThemeChanged = { newMode ->currentTheme = newMode},
                                onColourThemeChanged={newColourTheme ->currentColourTheme = newColourTheme}
                        )
                    }
                }
            }
        }
    }

}


