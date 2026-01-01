package com.niaw.launchnea.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niaw.launchnea.DataClasses.Agencies
import com.niaw.launchnea.DataClasses.LaunchReuseCrossRef
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Reuse
import com.niaw.launchnea.DataClasses.Rockets
import com.niaw.launchnea.DataClasses.Version

@Database(
    entities = [
        Agencies::class,
        Launches::class,
        LaunchReuseCrossRef::class,
        Reuse::class,
        Rockets::class,
        Version::class
        ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao

}