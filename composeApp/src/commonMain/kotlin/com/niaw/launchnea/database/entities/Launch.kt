package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.niaw.launchnea.Location
import com.niaw.launchnea.ProgramType
import com.niaw.launchnea.Status
import com.niaw.launchnea.Reusable
import com.niaw.launchnea.TimelineEvent
import com.niaw.launchnea.ui.UiElements.Chip
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ListBoxItem
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RocketVersion::class,
            parentColumns = ["rocketVersionID"],
            childColumns = ["rocketVersionID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Rocket::class,
            parentColumns = ["rocketID"],
            childColumns = ["rocketID"],
            onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Agency::class,
            parentColumns = ["agencyID"],
            childColumns = ["agencyID"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        Index("rocketVersionID"),
        Index("rocketID"),
        Index("agencyID")
    ]
)
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
    ): ListBoxItem {
    override fun listBoxText() = DisplayItems(missionName,"$agencyName | $rocketName", padName,launchTimeUTC.toString(),getChips())

    fun getChips():List<Chip>{
        var chips:List<Chip>
        reusableParts.forEach {reusable->
            chips.add(Chip(reuasable.abbrev))            
  
        }
        
    }
}
