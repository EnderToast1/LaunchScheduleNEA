package com.niaw.launchnea.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.MetaData
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion
import com.niaw.launchnea.database.entities.StarredLaunch
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    //Adds / Updates Entities
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
    @Upsert
    suspend fun upsertMetadata(data: MetaData)



    //Get MetaData
    @Query("SELECT value FROM MetaData WHERE data = :key")
    suspend fun getMetaData(key: String): String?


    //Get Flow Lists
    @Query("SELECT * FROM Launch")
    fun getLaunches(): Flow<List<Launch>>
    @Query("SELECT * FROM Launch WHERE launchTimeUTC < CURRENT_TIMESTAMP")
    fun getPreviousLaunches(): Flow<List<Launch>>
    @Query("SELECT * FROM Launch WHERE launchTimeUTC >= CURRENT_TIMESTAMP")
    fun getUpcomingLaunches(): Flow<List<Launch>>
    @Query("SELECT * FROM Rocket")
    fun getRockets(): Flow<List<Rocket>>
    @Query("SELECT * FROM Reuse")
    fun getReuse(): Flow<List<Reuse>>

    @Query("""
    SELECT Reuse.* FROM Reuse
    LEFT JOIN LaunchReuseCrossRef ON Reuse.reuseID = LaunchReuseCrossRef.reuseID
    WHERE Reuse.name NOT LIKE 'Unknown%' 
    GROUP BY Reuse.reuseID
    ORDER BY COUNT(LaunchReuseCrossRef.launchID) DESC
""")
    fun getReuseOrdered(): Flow<List<Reuse>>//The NOT 106 is to stop the Unknown booster showing up

    @Query("SELECT l.* FROM launch l INNER JOIN starred_launches s ON l.launchID = s.launchID")
    fun getStarredLaunches(): Flow<List<Launch>>
    @Transaction
    @Query("""
        SELECT * FROM Launch 
        INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID 
        WHERE LaunchReuseCrossRef.reuseID = :reuseID 
        AND Launch.launchTimeUTC >= CURRENT_TIMESTAMP
        ORDER BY Launch.launchTimeUTC ASC
    """)
    fun getUpcomingLaunchesByReuseId(reuseID: String): Flow<List<Launch>>
    @Transaction
    @Query("""
        SELECT * FROM Launch 
        INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID 
        WHERE LaunchReuseCrossRef.reuseID = :reuseID 
        AND Launch.launchTimeUTC < CURRENT_TIMESTAMP
        ORDER BY Launch.launchTimeUTC DESC
    """)
   fun getPreviousLaunchesByReuseId(reuseID: String): Flow<List<Launch>>



   //Get values for UI
   @Query("""
        SELECT COUNT(Launch.launchID) 
        FROM Launch
        INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID
        WHERE LaunchReuseCrossRef.reuseID = :reuseID 
        AND Launch.launchTimeUTC < CURRENT_TIMESTAMP
""")
    fun getReuseCountById(reuseID: String): Flow<Int>


    @Query("""
    WITH PastIntervals AS (
        SELECT 
            -- Convert String date to a Unix Timestamp (seconds)
            CAST(strftime('%s', launchTimeUTC) AS INTEGER) as startTime,
            LEAD(CAST(strftime('%s', launchTimeUTC) AS INTEGER)) OVER (ORDER BY launchTimeUTC ASC) as nextTime
        FROM Launch
        INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID
        WHERE LaunchReuseCrossRef.reuseID = :reuseID 
        -- Use CURRENT_TIMESTAMP to get Now
        AND Launch.launchTimeUTC < CURRENT_TIMESTAMP
    )
    -- The result is in seconds, so multiply by 1000 to return Milliseconds 
    SELECT AVG(nextTime - startTime) * 1000
    FROM PastIntervals 
    WHERE nextTime IS NOT NULL
""")
    fun getAverageReuseTime(reuseID: String): Flow<Long?>

    @Query("""
    WITH PastIntervals AS (
        SELECT 
            CAST(strftime('%s', launchTimeUTC) AS INTEGER) as startTime,
            LEAD(CAST(strftime('%s', launchTimeUTC) AS INTEGER)) OVER (ORDER BY launchTimeUTC ASC) as nextTime
        FROM Launch
        INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID
        WHERE LaunchReuseCrossRef.reuseID = :reuseID 
        AND Launch.launchTimeUTC < CURRENT_TIMESTAMP
    )
    SELECT MIN(nextTime - startTime) * 1000
    FROM PastIntervals 
    WHERE nextTime IS NOT NULL
""")
    fun getFastestReuseTime(reuseID: String): Flow<Long?>

    @Transaction
    @Query("""
    SELECT Launch.* FROM Launch
    INNER JOIN LaunchReuseCrossRef ON Launch.launchID = LaunchReuseCrossRef.launchID
    WHERE LaunchReuseCrossRef.reuseID = :reuseID 
    AND Launch.launchTimeUTC < CURRENT_TIMESTAMP
    ORDER BY Launch.launchTimeUTC DESC
    LIMIT 1
""")
    fun getLastFlightByReuseId(reuseID: String): Flow<Launch?>


    //Get singular item
    @Query("SELECT * FROM Launch WHERE launchID = :id")
    suspend fun getLaunchById(id: String): Launch?

    @Query("SELECT * FROM Agency WHERE agencyID = :id")
    suspend fun getAgencyById(id: String): Agency?

    @Query("SELECT * FROM Rocket WHERE rocketID = :id")
    suspend fun getRocketById(id: String): Rocket?

    @Query("SELECT * FROM RocketVersion WHERE rocketVersionID = :id")
    suspend fun getRocketVersionById(id: String): RocketVersion?

    @Query("SELECT * FROM Reuse WHERE reuseID = :id")
    suspend fun getReuseById(id: String): Reuse?

    //Stared Launch
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun starLaunch(star: StarredLaunch)
    @Query("DELETE FROM starred_launches WHERE launchId = :id")
    suspend fun unstarLaunch(id: String)
    @Query("SELECT EXISTS(SELECT 1 FROM starred_launches WHERE launchId = :id)")
    suspend fun isStarred(id: String): Boolean


    //Does Exist By ID
    @Query("SELECT EXISTS(SELECT 1 FROM Launch WHERE launchID = :id)")
    suspend fun doesLaunchExist(id: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM Agency WHERE agencyID = :id)")
    suspend fun doesAgencyExist(id: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM Reuse WHERE reuseID = :id)")
    suspend fun doesReuseExist(id: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM Rocket WHERE rocketID = :id)")
    suspend fun doesRocketExist(id: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM RocketVersion WHERE rocketVersionID = :id)")
    suspend fun doesRocketVersionExist(id: String): Boolean


    //Does Entity need Updating
    @Query("SELECT EXISTS(SELECT 1 FROM Agency WHERE agencyID = :id AND lastUpdated < :dateTime)")
    suspend fun doesAgencyNeedUpdating(id: String, dateTime: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM Rocket WHERE rocketID = :id AND lastUpdated < :dateTime)")
    suspend fun doesRocketNeedUpdating(id: String, dateTime: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM RocketVersion WHERE rocketVersionID = :id AND lastUpdated < :dateTime)")
    suspend fun doesRocketVersionNeedUpdating(id: String, dateTime: String): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM Reuse WHERE ReuseID = :id AND lastUpdated < :dateTime)")
    suspend fun doesReuseNeedUpdating(id: String, dateTime: String): Boolean


    //Search / Filter Systems
    @Query("SELECT * FROM launch WHERE strftime('%m-%d', launchTimeUTC) = strftime('%m-%d', 'now')")
    fun getOnThisDayLaunches(): Flow<List<Launch>>

    @Query("SELECT * FROM launch WHERE missionName LIKE '%' || :givenName || '%' COLLATE NOCASE")
    fun getLaunchLikeName(givenName: String): Flow<List<Launch>>//TODO split into past and upcoming

    @Query("SELECT * FROM launch WHERE missionName LIKE '%' || :givenName || '%' COLLATE NOCASE AND launchTimeUTC < CURRENT_TIMESTAMP")
    fun getPreviousLaunchesLikeName(givenName : String): Flow<List<Launch>>

    @Query("SELECT * FROM launch WHERE missionName LIKE '%' || :givenName || '%' COLLATE NOCASE AND launchTimeUTC > CURRENT_TIMESTAMP")
    fun getUpcomingLaunchesLikeName(givenName : String): Flow<List<Launch>>

    @Query("SELECT * FROM rocket WHERE name LIKE '%' || :givenName || '%' COLLATE NOCASE")
    fun getRocketLikeName(givenName: String): Flow<List<Rocket>>

    @Query("""SELECT Reuse.* FROM reuse
            LEFT JOIN LaunchReuseCrossRef ON Reuse.reuseID = LaunchReuseCrossRef.reuseID
            WHERE Reuse.name NOT LIKE 'Unknown%' 
               AND name LIKE '%' || :givenName || '%' COLLATE NOCASE 
            GROUP BY Reuse.reuseID
            ORDER BY COUNT(LaunchReuseCrossRef.launchID) DESC""")
    fun getReuseLikeName(givenName: String): Flow<List<Reuse>>


}

