package com.niaw.launchnea.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<ScheduleDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "schedule.db")
    return Room.databaseBuilder<ScheduleDatabase>(
        name = dbFile.absolutePath
    )
}

/*actual object ScheduleDatabaseConstructor : RoomDatabaseConstructor<ScheduleDatabase> {
    override fun initialize(): ScheduleDatabase = getScheduleDatabase(getDatabaseBuilder())
}*/