package com.niaw.launchnea

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.Screens.HomeScreen
import com.niaw.launchnea.navigation.Screens.LaunchesScreen
import com.niaw.launchnea.navigation.Screens.SettingsScreen
import com.niaw.launchnea.navigation.Screens.VehiclesScreen
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.FilledRocket
import launchnea.composeapp.generated.resources.filledrocketh
import org.jetbrains.compose.resources.painterResource

data class NavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
)

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App(scheduleDao: ScheduleDao) {
    val navController = rememberNavController()

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
                selectedIcon = {Icon(painterResource(Res.drawable.FilledRocket),"Rocket", modifier = Modifier.size(24.dp),
                    tint = LocalContentColor.current)},
                unselectedIcon = {Icon(painterResource(Res.drawable.FilledRocket),"Rocket", modifier = Modifier.size(24.dp),
                    tint = LocalContentColor.current)},
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

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        items.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                label = {Text(text = item.title)},
                                icon = {if (currentRoute == item.route) {item.selectedIcon()} else {item.unselectedIcon()}}
                            )
                        }
                    }
                }
            ) { innerPadding ->

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen()
                    }
                    composable("launches") {
                        LaunchesScreen()
                    }
                    composable("vehicles") {
                        VehiclesScreen()
                    }
                    composable("settings") {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}


