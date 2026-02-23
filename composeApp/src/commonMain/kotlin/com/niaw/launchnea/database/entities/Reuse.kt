package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.ReuseType
import com.niaw.launchnea.StatusReuse
import com.niaw.launchnea.ui.UiElements.DisplayItems
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
    override fun listBoxText() = DisplayItems(name, activeSince.toString(), status.toString(), rocketID)
}
