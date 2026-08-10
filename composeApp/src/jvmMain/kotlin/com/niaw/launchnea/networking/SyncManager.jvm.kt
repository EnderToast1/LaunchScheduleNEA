package com.niaw.launchnea.networking

actual class BackgroundSyncScheduler {
    actual fun schedulePeriodicBackgroundSync() {
        // Desktop doesn't need WorkManager. The coroutine loop handles desktop backgrounding.
    }

    actual fun cancelBackgroundSync() {
        // No-op for Desktop
    }
}