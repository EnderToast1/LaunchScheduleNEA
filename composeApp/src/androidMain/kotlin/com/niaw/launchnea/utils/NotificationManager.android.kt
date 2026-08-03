package com.niaw.launchnea.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.util.Log.e
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import io.ktor.util.collections.StringMap
import kotlin.jvm.java
import kotlin.random.Random

@SuppressLint("StaticFieldLeak")
object AndroidNotificationContext {
    lateinit var context: Context
}


@SuppressLint("StaticFieldLeak")
actual object NotificationManager {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    actual fun showNotification(
        title: String,
        message: String
    ) {
        val context = AndroidNotificationContext.context
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, "launch_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(Random.nextInt(), builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "launch_channel",
                "Launch Notifications",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )

            context.getSystemService(android.app.NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}




actual object NotificationScheduler {


    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    actual fun scheduleNotification(
        timeMillis: Long,
        title: String,
        message: String,
        requestCode: Int,
    ) {
        Log.d("NOTIFICATION", "Alarm Added")
        val context = AndroidNotificationContext.context
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Permission not granted
                // You can open the settings page here
                val intent = Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                ).apply {
                    data = "package:${context.packageName}".toUri()
                }

                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(
            context,
            NotificationReceiver::class.java
        ).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    actual fun cancelNotification(launchId: String) {
        Log.d("NOTIFICATION", "Alarm Cancelled")

        val context = AndroidNotificationContext.context

        val intent = Intent(
            context,
            NotificationReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            launchId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

}

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("NOTIFICATION", "Alarm Pre fire")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        try {
            NotificationManager.showNotification(
                intent.getStringExtra("title") ?: "",
                intent.getStringExtra("message") ?: ""
            )

            Log.d("NOTIFICATION", "Alarm Post fire")
        } catch (e: Exception) {
            Log.e("NOTIFICATION", "Failed to show notification", e)
        }
    }
    }