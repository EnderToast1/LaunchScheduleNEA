package com.niaw.launchnea.DataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
data class Launches(
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
    var imageUrl:  String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var weatherProbability: Float? =null,
    var agencyName: String,
    var rocketName: String,
    var rocketVariantName: String,
    var description: String,
    var orbit: String,
    var stats: Int, //Needs Changing can probably do from database
    var padCords: Location = Location(0.0, 0.0),
    var padLocation: String, //eg Cape Canaveral
    var padCountry: String,
    var padTimeZone: String,
    )

