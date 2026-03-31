package com.niaw.launchnea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.niaw.launchnea.database.getScheduleDatabase
import com.niaw.launchnea.Settings.AppSettings
import com.russhwolf.settings.SharedPreferencesSettings


object ActivityHolder {
    var current: ComponentActivity? = null
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ActivityHolder.current = this

        val sharedPrefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val settings = SharedPreferencesSettings(sharedPrefs)
        val themeSettings = AppSettings(settings)


        val dao = getScheduleDatabase(applicationContext).ScheduleDao()
        setContent {

            App(dao,themeSettings)
        }
    }
}

