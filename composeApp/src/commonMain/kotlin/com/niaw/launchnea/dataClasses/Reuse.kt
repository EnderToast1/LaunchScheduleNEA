package com.niaw.launchnea.DataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Reuse(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var status: Boolean=false,
    var flights: Int=0,
    var activeSince: Date? = null,
    var versionID:String,
    var rocketId:String,
    var agencyId:String,
    var reuseType: ReuseType = ReuseType.UNKNOWN
  )