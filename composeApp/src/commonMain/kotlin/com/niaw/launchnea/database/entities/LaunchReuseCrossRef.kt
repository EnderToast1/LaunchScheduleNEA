package com.niaw.launchnea.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["launchID", "reuseID"])
data class LaunchReuseCrossRef(
    var launchID: String,
    var reuseID: String,
)
