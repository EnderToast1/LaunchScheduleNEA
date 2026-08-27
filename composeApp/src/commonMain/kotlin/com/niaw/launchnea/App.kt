 package com.niaw.launchnea

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.niaw.launchnea.Settings.AppSettings
import com.niaw.launchnea.Settings.GlobalSettings
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.NavigationItem
import com.niaw.launchnea.networking.AppSyncManager
import com.niaw.launchnea.networking.BackgroundSyncScheduler
import com.niaw.launchnea.networking.updateLaunchData
import com.niaw.launchnea.ui.Screens.HomeScreens.HomeScreen
import com.niaw.launchnea.ui.Screens.LaunchScreens.LaunchesScreen
import com.niaw.launchnea.ui.Screens.SettingsScreen
import com.niaw.launchnea.ui.Screens.VehiclesScreens.VehiclesScreen
import com.niaw.launchnea.utils.AppTheme
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.filledrocketh
import org.jetbrains.compose.resources.painterResource


 @Suppress("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App(scheduleDao: ScheduleDao, scheduler: BackgroundSyncScheduler, appSettings: AppSettings) {

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

     val syncManager = remember {
         AppSyncManager(
             backgroundScheduler = scheduler,
             fetchApiCall = {
                 // API fetch & DB update call
                 updateLaunchData(scheduleDao)
             }
         )
     }


    AppTheme(selectedMode = currentTheme, seedColourType = currentColourTheme ) {

        LaunchedEffect(Unit) {
            syncManager.onAppForegrounded()//auto updates

                //updateLaunchData(scheduleDao)//Manual updates

        }
        PlatformLifecycleObserver(syncManager = syncManager)

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

 @Composable
 expect fun PlatformLifecycleObserver(syncManager: AppSyncManager)
