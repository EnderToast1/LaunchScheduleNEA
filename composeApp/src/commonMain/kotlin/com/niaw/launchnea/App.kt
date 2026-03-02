package com.niaw.launchnea

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.networking.updateLaunchData
import com.niaw.launchnea.ui.Screens.HomeScreens.HomeScreen
import com.niaw.launchnea.ui.Screens.LaunchScreens.LaunchesScreen
import com.niaw.launchnea.ui.Screens.SettingsScreen
import com.niaw.launchnea.ui.Screens.VehiclesScreens.VehiclesScreen
import com.niaw.launchnea.navigation.NavigationItem
import kotlinx.coroutines.launch
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.filledrocketh
import org.jetbrains.compose.resources.painterResource


@Suppress("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App(scheduleDao: ScheduleDao) {
    var currentRoute by rememberSaveable  { mutableStateOf("home") }





    MaterialTheme {
        LaunchedEffect(Unit) {
            //updateLaunchData(scheduleDao)
            //daoTest(scheduleDao)
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
                        "home"     -> HomeScreen()
                        "launches" -> LaunchesScreen(scheduleDao)
                        "vehicles" -> VehiclesScreen(scheduleDao)
                        "settings" -> SettingsScreen()
                    }
                }
            }
        }
    }
}


