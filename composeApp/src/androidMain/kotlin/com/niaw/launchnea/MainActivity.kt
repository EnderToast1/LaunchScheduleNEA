package com.niaw.launchnea

import android.Manifest
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.niaw.launchnea.database.getScheduleDatabase
import com.niaw.launchnea.Settings.AppSettings
import com.niaw.launchnea.utils.NotificationManager
import com.russhwolf.settings.SharedPreferencesSettings
import android.os.Build
import com.niaw.launchnea.utils.AndroidNotificationContext

object ActivityHolder {
    var current: ComponentActivity? = null
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ActivityHolder.current = this


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }


        val sharedPrefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val settings = SharedPreferencesSettings(sharedPrefs)
        val themeSettings = AppSettings(settings)


        val dao = getScheduleDatabase(applicationContext).ScheduleDao()
        setContent {

            App(dao,themeSettings)
        }
    }
}


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidNotificationContext.context = applicationContext
    }
}