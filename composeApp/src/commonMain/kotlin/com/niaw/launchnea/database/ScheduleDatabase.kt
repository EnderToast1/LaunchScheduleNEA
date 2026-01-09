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

//creating the database object
@Database(
    //passes a list of all the tables and what they are
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
    //gives a version number so if a update is needed it can tell and try migrate the db
    version = 1,
)
//tell the db where the type converters are
@TypeConverters(Converters::class)
abstract class ScheduleDatabase: RoomDatabase() {
    //give the db it functions
    abstract fun ScheduleDao(): ScheduleDao

}
