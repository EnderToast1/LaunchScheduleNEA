package com.niaw.launchnea.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import java.io.File

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ScheduleDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("schedule.db")  // Proper private location on Android
    return Room.databaseBuilder<ScheduleDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

// ScheduleDatabaseConstructor.android.kt (or same file)
/*actual object ScheduleDatabaseConstructor : RoomDatabaseConstructor<ScheduleDatabase> {
    override fun initialize(): ScheduleDatabase {
        // You'll pass the builder from your app code (see usage below)
        error("Call getScheduleDatabase(getDatabaseBuilder(context)) instead")
    }
}*/