package com.niaw.launchnea

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.niaw.launchnea.networking.AppSyncManager

@Composable
actual fun PlatformLifecycleObserver(syncManager: AppSyncManager) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> syncManager.onAppForegrounded() // 5-min Coroutine loop starts
                Lifecycle.Event.ON_STOP -> syncManager.onAppBackgrounded()  // Switch to WorkManager
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}