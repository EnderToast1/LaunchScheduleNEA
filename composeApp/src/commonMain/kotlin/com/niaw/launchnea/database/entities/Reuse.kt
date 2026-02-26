package com.niaw.launchnea.database.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.outlined.AirplanemodeActive
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.ReuseType
import com.niaw.launchnea.StatusReuse
import com.niaw.launchnea.ui.UiElements.DisplayIcon
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ImageGroup
import com.niaw.launchnea.ui.UiElements.ListBoxItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity(foreignKeys = [
    ForeignKey(
        entity = RocketVersion::class,
        parentColumns = ["rocketVersionID"],
        childColumns = ["versionID"],
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
    )
])
data class Reuse(
    @PrimaryKey(autoGenerate = false)
    var reuseID: String,
    var name: String? = null,
    var activeSince: LocalDate? = null,
    var versionID:String,
    var rocketID:String? = null,
    var agencyID:String? = null,
    var reuseType: ReuseType = ReuseType.UNKNOWN,
    var status: StatusReuse = StatusReuse.UNKNOWN,
    var lastUpdated: LocalDateTime?=null,
  ): ListBoxItem {

    //TODO add RocketImage in ImageGroup and change show to true
    override fun listBoxText() = DisplayItems(
        title = name,
        imageGroup = ImageGroup(show = false),
        secondRowIcon = DisplayIcon.Vector(Icons.Outlined.Timer),
        secondRow = activeSince.toString(),
        thirdRowIcon = statusReuseIcon(),
        thirdRow = status.toString(),
        fourthRowIcon = DisplayIcon.Vector(Icons.Filled.Rocket),
        fourthRow = rocketID
    )

    fun statusReuseIcon(): DisplayIcon.Vector {//TODO need Editing
        return when (this.status) {
            StatusReuse.RETIRED,
            StatusReuse.EXPENDED -> DisplayIcon.Vector(Icons.Outlined.CheckCircle)
            StatusReuse.ACTIVE -> DisplayIcon.Vector(Icons.Outlined.AirplanemodeActive)
            StatusReuse.LOST -> DisplayIcon.Vector(Icons.Outlined.Error)
            StatusReuse.SCRAPPED -> DisplayIcon.Vector(Icons.Outlined.Delete)
            StatusReuse.CONVERTED -> DisplayIcon.Vector(Icons.Outlined.SwapHoriz)
            StatusReuse.DESTROYED -> DisplayIcon.Vector(Icons.Outlined.Cancel)
            StatusReuse.UNKNOWN -> DisplayIcon.Vector(Icons.AutoMirrored.Outlined.Help)
        }
    }
}