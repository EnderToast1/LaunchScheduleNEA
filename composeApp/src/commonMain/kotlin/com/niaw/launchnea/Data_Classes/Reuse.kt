package com.niaw.launchnea.Data_Classes

import kotlinx.serialization.Serializable
import java.util.Date


data class Reuse(
    var id: String,
    var name: String,
    var status: Boolean,
    var flights: Int=0,
    var activeSince: Date? = null,
    var versionId:String,
    var rocketId:String,
    var agencyId:String,
    var reuseType: ReuseType = ReuseType.UNKNOWN
  )