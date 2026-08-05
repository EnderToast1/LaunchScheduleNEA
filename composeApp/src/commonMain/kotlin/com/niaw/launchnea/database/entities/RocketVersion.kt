package com.niaw.launchnea.database.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Schedule
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.Settings.GlobalSettings.dateFormat
import com.niaw.launchnea.ui.UiElements.*
import launchnea.composeapp.generated.resources.Res
import launchnea.composeapp.generated.resources.search_book
import launchnea.composeapp.generated.resources.check_circle
import launchnea.composeapp.generated.resources.inventory_2
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    var stages: Int? = null,
    var imageURL: String? =null,
    var imageThumbURL: String? =null,
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var lastUpdated: LocalDateTime?=null,

    ): ListBoxItem {


    override fun listBoxText() = DisplayItems(
        id=rocketVersionID,
        title =this.name,
        imageGroup = ImageGroup(this.imageThumbURL, imageCredit,licenseType,true),
        secondRowIcon = DisplayIcon.Vector(Icons.Filled.Schedule),
        secondRow = "Active Since ${activeSince?.format(DateTimeFormatter.ofPattern(dateFormat))}",
        thirdRowIcon = DisplayIcon.Vector(Icons.Filled.Numbers),
        thirdRow = "$launches Flight${if (launches == 1) "" else "s"}",
        chips = getChips(),
    )

    fun getChips():List<Chip>  = listOf(Chip(if (active == true) "Active" else "Retired",null,if (active == true) true else false,if (active == true) Res.drawable.check_circle else Res.drawable.inventory_2))
}

