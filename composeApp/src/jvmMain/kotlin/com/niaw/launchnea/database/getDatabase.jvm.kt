package com.niaw.launchnea.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getDatabase(): ScheduleDatabase {
    val dbFile = File(System
            .getProperty("java.io.tmpdir"), "schedule.db")
    return Room.databaseBuilder<ScheduleDatabase>(
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).build()
}
