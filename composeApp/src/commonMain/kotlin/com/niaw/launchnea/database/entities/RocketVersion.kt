package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.ui.UiElements.Chip
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ImageGroup
import com.niaw.launchnea.ui.UiElements.ListBoxItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [
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
        )]
)
data class RocketVersion(
    @PrimaryKey(autoGenerate = false)
    var rocketVersionID: String,
    var name: String? = null,
    var active: Boolean? = null,
    var launches: Int? = null,
    var activeSince: LocalDate? = null,
    var rocketID: String? = null,
    var agencyID: String? = null,
    var diameterMe: Double? = null,
    var lengthMe: Double? = null,
    var launchCost: Int? = null,
    var launchMass: Double? = null,
    var leoCapacity: Double? = null,
    var gtoCapacity: Double? = null,
    var liftoffThrust: Double? = null,


    var imageURL: String? =null,
    var imageThumbURL: String? =null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var lastUpdated: LocalDateTime?=null,

    ): ListBoxItem {
    override fun listBoxText(scheduleDao: ScheduleDao?) = DisplayItems(
        title =this.name,
        imageGroup = ImageGroup(this.imageThumbURL, imageCredit,licenseType,true),

        secondRow = "$activeSince",
        thirdRow = "20",
        chips = getChips(),
    )

    fun getChips():List<Chip>  = listOf(Chip(if (active == true) "Active" else "Retired"))
}

