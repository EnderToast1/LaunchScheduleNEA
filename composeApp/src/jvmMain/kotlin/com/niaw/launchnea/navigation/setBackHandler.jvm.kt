package com.niaw.launchnea.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun SetBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Not needed for JVM/desktop
}