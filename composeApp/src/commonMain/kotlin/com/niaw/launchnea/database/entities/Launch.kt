package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.niaw.launchnea.Location
import com.niaw.launchnea.ProgramType
import com.niaw.launchnea.Status
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.TimelineEvent
import kotlinx.serialization.json.JsonObject
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
data class Launch(
    //database search and filter terms
    @PrimaryKey(autoGenerate = false)
    val launchID: String,
    val launchURL: String?=null,
    val missionName: String? = null,
    val padName: String? = null,
    val agencyID: String? = null,
    val rocketID: String? = null,
    val rocketVersionID: String? = null,
    val launchTimeUTC: ZonedDateTime? = null,
    val launchTimePadLocal: ZonedDateTime? = null,
    var reusableParts: List<Reusable> = emptyList(),


    //Rest of data needed
    var success: Status = Status.UNKNOWN,
    var windowOpen: ZonedDateTime? = null,
    var windowClose: ZonedDateTime? = null,
    var imageUrl: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var weatherProbability: Double? =null,
    var agencyName: String? = null,
    var rocketName: String? = null,
    var rocketVariantName: String? = null,
    var description: String? = null,
    var orbit: String? =null,
    var padCords: Location = Location(0.0, 0.0),
    var padLocation: String? = null, //eg Cape Canaveral
    var padCountry: String? = null,
    var padTimeZone: String? = null,
    var liveStreamUrl: String? = null,
    var netPrecision: String? = null,
    var weatherConcerns: String? = null,
    var timeline: List<TimelineEvent> = emptyList(),
    var precisionAbbrev: String? = null,
    val programType: ProgramType?= ProgramType.UNKNOWN
    )

