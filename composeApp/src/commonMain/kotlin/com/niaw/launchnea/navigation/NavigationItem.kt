package com.niaw.launchnea.navigation

import androidx.compose.runtime.Composable

data class NavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
)