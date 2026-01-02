package com.niaw.launchnea.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Upsert
    suspend fun upsertAgency(agency: Agency)

    @Upsert
    suspend fun upsertLaunch(launch: Launch)

    @Upsert
    suspend fun upsertReuse(reuse: Reuse)

    @Upsert
    suspend fun upsertRocket(rocket: Rocket)

    @Upsert
    suspend fun upsertRocketVersion(rocketVersion: RocketVersion)

    @Upsert
    suspend fun upsertLaunchReuseCrossRef(crossRef: LaunchReuseCrossRef)

    @Delete
    suspend fun deleteLaunches(launch: Launch)

    @Query("SELECT * FROM Launch")
    fun getLaunches(): Flow<List<Launch>>

}