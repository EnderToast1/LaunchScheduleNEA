@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.niaw.launchnea.database

// 2. TypeConverters for complex types
import androidx.room.*
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.niaw.launchnea.Data_Classes.Location
import com.niaw.launchnea.Data_Classes.ResultsState
import com.niaw.launchnea.Data_Classes.Reusables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZonedDateTime


@Entity(tableName = "launches")
data class Launch(
    @PrimaryKey val id: String,

    // Search & filter terms
    val missionName: String,
    val padName: String,
    val agencyID: String,
    val rocketID: String,
    val rocketVersionID: String,

    // Times
    val launchTimeUTC: ZonedDateTime,
    val launchTimePadLocal: LocalDateTime,

    // Reusable parts (stored as JSON)
    var reusableParts: List<Reusables> = emptyList(),

    // Outcome
    var success: ResultsState = ResultsState.UNKNOWN,

    // Launch window
    val windowOpen: ZonedDateTime,
    val windowClose: ZonedDateTime,

    // Image & credits
    var imageUrl: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,

    // Weather & names
    var weatherProbability: Float? = null,
    val agencyName: String,
    val rocketName: String,
    val rocketVariantName: String,

    // Description & orbit
    val description: String,
    val orbit: String,

    // Stats (you can compute this later from reusableParts.count or DB queries)
    var stats: Int = 0,

    // Pad location details
    val padCords: Location,
    val padLocation: String,      // e.g., "Cape Canaveral"
    val padCountry: String,
    val padTimeZone: String
)


@Dao
interface LaunchDao {
    @Query("SELECT * FROM launches ORDER BY missionName ASC")
    fun getAllLaunches(): Flow<List<Launch>>

    @Query("SELECT * FROM launches WHERE id = :id")
    suspend fun getLaunchById(id: String): Launch?

    @Insert
    suspend fun insertLaunch(launch: Launch)

    @Insert
    suspend fun insertLaunches(launches: List<Launch>)

    @Update
    suspend fun updateLaunch(launch: Launch)

    @Query("DELETE FROM launches WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM launches")
    suspend fun deleteAll()

    // Search/filter queries (using your specified fields)
    @Query("""
        SELECT * FROM launches 
        WHERE missionName LIKE '%' || :query || '%' 
           OR padName LIKE '%' || :query || '%' 
           OR agencyID LIKE '%' || :query || '%' 
           OR rocketID LIKE '%' || :query || '%' 
           OR rocketVersionID LIKE '%' || :query || '%'
        ORDER BY missionName ASC
    """)
    fun searchLaunches(query: String): Flow<List<Launch>>
}

@Database(entities = [Launch::class], version = 2, exportSchema = true)  // Increment version!
@TypeConverters(Converters::class)  // Apply globally
@ConstructedBy(ScheduleDatabaseConstructor::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
}

// This must be 'expect' – Room generates the actuals
@Suppress("NO_ACTUAL_FOR_EXPECT")  // Suppresses the IDE warning
expect object ScheduleDatabaseConstructor : RoomDatabaseConstructor<ScheduleDatabase>

fun getScheduleDatabase(builder: RoomDatabase.Builder<ScheduleDatabase>): ScheduleDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

