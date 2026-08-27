package com.niaw.launchnea.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

@Volatile
private var instance: ScheduleDatabase? = null

fun getScheduleDatabase(context: Context): ScheduleDatabase {
    return instance ?: synchronized(ScheduleDatabase::class.java) {
        instance ?: Room.databaseBuilder<ScheduleDatabase>(
            context = context.applicationContext,
            name = "schedule.db"
        )
            .createFromAsset("schedule.db")
            .build()
            .also { instance = it }
    }
}