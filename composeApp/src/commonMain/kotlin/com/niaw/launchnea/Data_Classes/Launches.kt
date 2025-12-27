package com.niaw.launchnea.Data_Classes

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class Launches(
    //database search and filter terms
    val id: String,
    val missionName: String,
    val padName: String,
    val agencyID: String,
    val rocketID: String,
    val rocketVersionID: String,
    val launchTimeUTC: ZonedDateTime,
    val launchTimePadLocal: LocalDateTime,
    var reusableParts: List<Reusables>,


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
    var description: String, //LongText
    var orbit: String,
    var stats: Int, //Needs Changing can probably do from database
    var padCords: Location,
    var padLocation: String, //eg cape Canaveral
    var padCountry: String,
    var padTimeZone: String,
    )