package com.niaw.launchnea.networking

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect class BackgroundSyncScheduler {
    fun schedulePeriodicBackgroundSync()
    fun cancelBackgroundSync()
}

object SyncIntervals {
    val foreground: Duration = 5.minutes
    /** Target interval between background syncs (exact alarm on Android). */
    val background: Duration = 15.minutes
}

private val nextCallFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

fun logNextApiCall(source: String, interval: Duration) {
    val nextAt = LocalDateTime.now().plusSeconds(interval.inWholeSeconds).format(nextCallFormatter)
    println(
        "Next $source API call in ${interval.inWholeMinutes} min " +
            "(~${interval.inWholeSeconds}s) at $nextAt"
    )
}

/** Process-wide lock so foreground loop and WorkManager never sync at the same time. */
object SyncGate {
    private val mutex = Mutex()

    suspend fun runExclusive(reason: String, block: suspend () -> Unit) {
        if (!mutex.tryLock()) {
            println("Sync already in progress ($reason), skipping")
            return
        }
        try {
            println("Starting sync ($reason)")
            block()
        } finally {
            mutex.unlock()
        }
    }
}

class AppSyncManager(
    private val backgroundScheduler: BackgroundSyncScheduler,
    private val fetchApiCall: suspend () -> Unit
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var foregroundJob: Job? = null

    // Call when app moves to FOREGROUND
    fun onAppForegrounded() {
        // Both Compose startup and the Android lifecycle can report foregrounding.
        // Keep the request already in progress: replacing its job can make the new
        // loop lose the SyncGate and delay the next real update by five minutes.
        if (foregroundJob?.isActive == true) return

        // Stop periodic background work; foreground loop owns syncing now
        backgroundScheduler.cancelBackgroundSync()

        foregroundJob = scope.launch {
            while (isActive) {
                try {
                    // The first loop iteration is immediate, refreshing cached data
                    // whenever the app is opened or brought to the foreground.
                    SyncGate.runExclusive("foreground") { fetchApiCall() }
                    logNextApiCall("foreground", SyncIntervals.foreground)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    println("Foreground fetch error: ${e.message}")
                    logNextApiCall("foreground", SyncIntervals.foreground)
                }
                delay(SyncIntervals.foreground)//Time Between Calls
            }
        }
    }

    // Call when app moves to BACKGROUND or closes
    fun onAppBackgrounded() {
        // Stop the in-app coroutine loop
        foregroundJob?.cancel()
        foregroundJob = null

        // Enqueue background scheduler for when the phone is locked/idle
        backgroundScheduler.schedulePeriodicBackgroundSync()
    }
}
