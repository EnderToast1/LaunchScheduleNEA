package com.niaw.launchnea.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.niaw.launchnea.DataClasses.Agencies
import com.niaw.launchnea.DataClasses.LaunchReuseCrossRef
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Reuse
import com.niaw.launchnea.DataClasses.Rockets
import com.niaw.launchnea.DataClasses.Version
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Upsert
    suspend fun upsertAgency(agency: Agencies)

    @Upsert
    suspend fun upsertLaunches(launches: Launches)

    @Upsert
    suspend fun upsertReuse(reuse: Reuse)

    @Upsert
    suspend fun upsertRocket(rocket: Rockets)

    @Upsert
    suspend fun upsertVersion(version: Version)

    @Upsert
    suspend fun upsertLaunchReuseCrossRef(CrossRef: LaunchReuseCrossRef)

    @Delete
    suspend fun deleteLaunches(launches: Launches)

    @Query("SELECT * FROM Launches")
    fun getLaunches(): Flow<List<Launches>>

}