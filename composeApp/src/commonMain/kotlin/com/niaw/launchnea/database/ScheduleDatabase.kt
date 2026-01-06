package com.niaw.launchnea.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.MetaData
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.database.entities.StarredLaunch

@Database(
    entities = [
        Agency::class,
        Launch::class,
        LaunchReuseCrossRef::class,
        Reuse::class,
        Rocket::class,
        RocketVersion::class,
        StarredLaunch::class,
        MetaData::class
        ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao

}