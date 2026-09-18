package com.niaw.launchnea.navigation

import androidx.compose.runtime.Composable


@Composable
expect fun SetBackHandler(enabled: Boolean, onBack: () -> Unit)