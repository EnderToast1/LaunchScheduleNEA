package com.niaw.launchnea.Data_Classes

import java.util.Date

data class Version(
    var id: String,
    var name: String,
    var active: Boolean,
    var flights: Int,
    var activeSince: Date? = null,
    var rocketId: String,
    var agencyId: String,

    var imageURL: String? =null,


    )
