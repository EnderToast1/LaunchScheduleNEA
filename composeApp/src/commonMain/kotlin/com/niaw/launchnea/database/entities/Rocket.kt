package com.niaw.launchnea.database.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.material.icons.outlined.Timer
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.ui.UiElements.DisplayIcon
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ImageGroup
import com.niaw.launchnea.ui.UiElements.ListBoxItem
import com.niaw.launchnea.utils.Status
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Agency::class,
            parentColumns = ["agencyID"],
            childColumns = ["agencyID"],
            onDelete = ForeignKey.CASCADE
        )
])
data class Rocket(
    @PrimaryKey(autoGenerate = false)
    var rocketID: String,
    var name: String?= null,
    var agencyID: String? = null,
    var agencyName: String? = null,
    var imageURL: String? = null,
    var imageThumbURL: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var desc: String? =null, //long
    var lastUpdated: LocalDateTime?=null
  )
    : ListBoxItem {
    override fun listBoxText() = DisplayItems(
            id=rocketID,
            title = this.name,
            imageGroup = ImageGroup(imageThumbURL, imageCredit, licenseType,true),
            secondRowIcon = DisplayIcon.Vector(Icons.Outlined.Business),
            secondRow = this.agencyName,
            thirdRowIcon = DisplayIcon.Vector(Icons.Outlined.Numbers),
            thirdRow = "Flights",
            fourthRowIcon = DisplayIcon.Vector(Icons.Outlined.History),
            fourthRow = "Time since last flight",
            success= Status.UNKNOWN
    )
}
