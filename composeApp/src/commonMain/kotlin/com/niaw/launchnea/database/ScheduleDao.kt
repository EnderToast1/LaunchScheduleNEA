package com.niaw.launchnea.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.database.entities.StarredLaunch
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

    @Query("SELECT EXISTS(SELECT 1 FROM Launch WHERE launchID = :id)")
    suspend fun launchExistsById(id: String): Boolean

    @Query("SELECT * FROM Launch WHERE launchID = :id")
    suspend fun getLaunchById(id: String): Launch?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun starLaunch(star: StarredLaunch)

    @Query("DELETE FROM starred_launches WHERE launchId = :id")
    suspend fun unstarLaunch(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM starred_launches WHERE launchId = :id)")
    suspend fun isStarred(id: String): Boolean

    @Query("SELECT l.* FROM launch l INNER JOIN starred_launches s ON l.launchID = s.launchID")
    fun getStarredLaunches(): Flow<List<Launch>>


}