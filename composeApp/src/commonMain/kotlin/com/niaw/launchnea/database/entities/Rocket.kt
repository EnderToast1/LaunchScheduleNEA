package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.ui.UiElements.DisplayItems
import com.niaw.launchnea.ui.UiElements.ListBoxItem
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

    var imageURL: String? = null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var desc: String? =null, //long
    var lastUpdated: LocalDateTime?=null,
  ): ListBoxItem {
    override fun listBoxText() = DisplayItems(this.name,this.agencyID,"Flights", "Time since last fight")
}
