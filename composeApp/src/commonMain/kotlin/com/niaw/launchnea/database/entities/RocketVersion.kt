package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    var imageCredit: String? = null,
    var licenseType: String? = null,
    var lastUpdated: LocalDateTime?=null,

    )
