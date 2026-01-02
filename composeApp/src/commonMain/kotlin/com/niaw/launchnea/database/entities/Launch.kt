package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.niaw.launchnea.Location
import com.niaw.launchnea.ResultsState
import com.niaw.launchnea.Reusable
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
data class Launch(
    //database search and filter terms
    @PrimaryKey(autoGenerate = false)
    val launchID: String,
    val missionName: String,
    val padName: String,
    val agencyID: String,
    val rocketID: String,
    val rocketVersionID: String,
    val launchTimeUTC: ZonedDateTime,
    val launchTimePadLocal: LocalDateTime,
    var reusableParts: List<Reusable> = emptyList(),


    //Rest of data needed
    var success: ResultsState = ResultsState.UNKNOWN,
    var windowOpen: ZonedDateTime,
    var windowClose: ZonedDateTime,
    var imageUrl: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var weatherProbability: Double? =null,
    var agencyName: String,
    var rocketName: String,
    var rocketVariantName: String,
    var description: String,
    var orbit: String,
    var padCords: Location = Location(0.0, 0.0),
    var padLocation: String, //eg Cape Canaveral
    var padCountry: String,
    var padTimeZone: String,
    )

