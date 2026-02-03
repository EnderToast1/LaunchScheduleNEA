package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.niaw.launchnea.ReuseType
import com.niaw.launchnea.StatusReuse
import java.time.LocalDate

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
    var lastUpdated: ZonedDateTime?=null
  )
