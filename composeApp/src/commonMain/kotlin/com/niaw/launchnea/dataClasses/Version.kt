package com.niaw.launchnea.DataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Version(
    @PrimaryKey(autoGenerate = false)
    var rocketVersionID: String,
    var name: String,
    var active: Boolean,
    var flights: Int,
    var activeSince: Date? = null,
    var rocketID: String,
    var agencyID: String,

    var imageURL: String? =null,


    )
