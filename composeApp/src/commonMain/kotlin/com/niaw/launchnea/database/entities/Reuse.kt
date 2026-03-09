seepackage com.niaw.launchnea.database.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.outlined.AirplanemodeActive
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.database.ScheduleDao
import com.niaw.launchnea.navigation.ViewModels.ReuseBoxInfo
import com.niaw.launchnea.navigation.ViewModels.ReuseViewModel
import com.niaw.launchnea.ui.UiElements.Chip
import com.niaw.launchnea.utils.ReuseType
import com.niaw.launchnea.utils.StatusReuse
import com.niaw.launchnea.ui.UiElements.DisplayIcon
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ImageGroup
import com.niaw.launchnea.ui.UiElements.ListBoxItem
import java.time.LocalDate
import java.time.LocalDateTime

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
    var rocketName:String? = null,
    var agencyName:String? = null,
    var reuseType: ReuseType = ReuseType.UNKNOWN,
    var status: StatusReuse = StatusReuse.UNKNOWN,
    var lastUpdated: LocalDateTime?=null,
    var imageURL: String? = null,
    var imageThumbURL: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
  ): ListBoxItem {

    override fun listBoxText(scheduleDao: ScheduleDao?): 

            return DisplayItems(
                id=reuseID,
                title = name,
                imageGroup = ImageGroup(imageThumbURL, imageCredit, licenseType, show = true),
                secondRowIcon = DisplayIcon.Vector(Icons.Outlined.Rocket),
                secondRow = "$rocketName|$agencyName",
                thirdRowIcon = statusReuseIcon(),
                thirdRow = status.toString(),
                fourthRowIcon = DisplayIcon.Vector(Icons.Outlined.History),
                fourthRow ="Test"  "scheduleDao?.getLastFlightByReuseId(reuseID)",
                chips = listOf(Chip(reuseID)),
                success=Status.UNKOWN
            )
        }
    return DisplayItems()}

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
