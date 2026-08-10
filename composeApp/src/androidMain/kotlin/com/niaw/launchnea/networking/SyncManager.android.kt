package com.niaw.launchnea.networking

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.niaw.launchnea.database.getScheduleDatabase
import kotlinx.coroutines.CancellationException
import java.util.concurrent.TimeUnit

actual class BackgroundSyncScheduler(private val context: Context) {
    private val appContext = context.applicationContext
    private val workManager = WorkManager.getInstance(appContext)

    actual fun schedulePeriodicBackgroundSync() {
        enabled = true
        scheduleNext(SyncIntervals.background.inWholeMilliseconds)
        logNextApiCall("background", SyncIntervals.background)
    }

    /** Call after a background sync to chain the next run. */
    fun scheduleNextAfterSync() {
        if (!enabled) {
            println("Background sync disabled (app foregrounded) — not re-arming")
            return
        }
        scheduleNext(SyncIntervals.background.inWholeMilliseconds)
        logNextApiCall("background", SyncIntervals.background)
    }

    /** Hand off to a foreground Worker immediately (safe from a BroadcastReceiver). */
    fun enqueueSyncNow() {
        ensureSyncNotificationChannel(appContext)

        val request = OneTimeWorkRequestBuilder<ApiSyncWorker>()
            .setConstraints(networkConstraints())
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
        println("Background sync enqueued (foreground worker)")
    }

    actual fun cancelBackgroundSync() {
        enabled = false
        alarmManager().cancel(alarmPendingIntent())
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
        println("Background sync cancelled")
    }

    private fun scheduleNext(delayMs: Long) {
        val triggerAt = System.currentTimeMillis() + delayMs
        val alarmManager = alarmManager()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                enqueueDelayedWork(delayMs)
                return
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                alarmPendingIntent()
            )
            println("Background exact alarm armed (delay ${delayMs / 1000}s)")
        } catch (_: SecurityException) {
            enqueueDelayedWork(delayMs)
        }
    }

    private fun enqueueDelayedWork(delayMs: Long) {
        ensureSyncNotificationChannel(appContext)

        val request = OneTimeWorkRequestBuilder<ApiSyncWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setConstraints(networkConstraints())
            .build()

        workManager.enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
        println(
            "No exact-alarm permission — fallback WorkManager delay " +
                "${delayMs / 60000} min (may still be deferred by Android)"
        )
    }

    private fun networkConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private fun alarmManager() =
        appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun alarmPendingIntent(): PendingIntent {
        val intent = Intent(appContext, SyncAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            appContext,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val UNIQUE_WORK_NAME = "ApiSyncWork"
        const val SYNC_CHANNEL_ID = "sync_channel"
        const val SYNC_NOTIFICATION_ID = 71002
        private const val ALARM_REQUEST_CODE = 71001

        @Volatile
        var enabled: Boolean = false
            private set
    }
}

fun ensureSyncNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(NotificationManager::class.java)
    val channel = NotificationChannel(
        BackgroundSyncScheduler.SYNC_CHANNEL_ID,
        "Data sync",
        NotificationManager.IMPORTANCE_LOW
    ).apply {
        description = "Shows while launch data is updating in the background"
    }
    manager.createNotificationChannel(channel)
}

fun buildSyncForegroundInfo(context: Context): ForegroundInfo {
    ensureSyncNotificationChannel(context)
    val notification = NotificationCompat.Builder(context, BackgroundSyncScheduler.SYNC_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.stat_notify_sync)
        .setContentTitle("Updating launches")
        .setContentText("Fetching the latest launch data…")
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        ForegroundInfo(
            BackgroundSyncScheduler.SYNC_NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    } else {
        ForegroundInfo(BackgroundSyncScheduler.SYNC_NOTIFICATION_ID, notification)
    }
}

suspend fun performBackgroundApiSync(context: Context) {
    val appContext = context.applicationContext
    try {
        SyncGate.runExclusive("background") {
            val dao = getScheduleDatabase(appContext).ScheduleDao()
            updateLaunchData(dao)
        }
    } catch (e: Exception) {
        println("Background sync error: ${e.message}")
    } finally {
        BackgroundSyncScheduler(appContext).scheduleNextAfterSync()
    }
}

/**
 * Alarm only wakes us and hands off — must return immediately to avoid ANR.
 * Long work runs in [ApiSyncWorker] as a foreground service.
 */
class SyncAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        println("Background sync alarm fired — handing off to worker")
        BackgroundSyncScheduler(context.applicationContext).enqueueSyncNow()
    }
}

class ApiSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        buildSyncForegroundInfo(applicationContext)

    override suspend fun doWork(): Result {
        return try {
            // Promote to foreground ASAP so long API/DB work is allowed
            setForeground(buildSyncForegroundInfo(applicationContext))
            performBackgroundApiSync(applicationContext)
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("Background WorkManager sync error: ${e.message}")
            Result.failure()
        }
    }
}
