package com.niaw.launchnea.DataClasses

import androidx.room.Entity

@Entity(primaryKeys = ["launchID", "reuseID"])
data class LaunchReuseCrossRef(
    var launchID: String,
    var reuseID: String,
)
