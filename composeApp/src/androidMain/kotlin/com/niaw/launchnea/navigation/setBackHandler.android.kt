package com.niaw.launchnea.navigation


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun SetBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}