package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    var lastUpdated: ZonedDateTime?=null
  )
