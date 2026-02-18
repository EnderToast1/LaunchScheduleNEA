package com.niaw.launchnea.ui

import androidx.compose.runtime.Composable

data class NavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
)
