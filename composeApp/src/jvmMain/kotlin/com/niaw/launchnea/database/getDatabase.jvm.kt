package com.niaw.launchnea.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

private var instance: ScheduleDatabase? = null

fun getDatabase(): ScheduleDatabase {
    return instance ?: synchronized(ScheduleDatabase::class.java) {

        instance ?: run {

            val appData = File(
                System.getProperty("user.home"),
                ".launchnea"
            )

            appData.mkdirs()

            val dbFile = File(appData, "schedule.db")

            if (!dbFile.exists()) {
                val inputStream = object {}.javaClass
                    .classLoader
                    ?.getResourceAsStream("schedule.db")
                    ?: error("Preloaded schedule.db not found")

                inputStream.use { input ->
                    dbFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            Room.databaseBuilder<ScheduleDatabase>(
                name = dbFile.absolutePath
            )
                .setDriver(BundledSQLiteDriver())
                .build()
                .also { instance = it }
        }
    }
}