package com.niaw.launchnea

import androidx.compose.runtime.Composable
import com.niaw.launchnea.networking.AppSyncManager

@Composable
actual fun PlatformLifecycleObserver(syncManager: AppSyncManager) {
    // Desktop keeps the coroutine loop running; no lifecycle switching needed
}