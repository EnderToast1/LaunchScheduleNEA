package com.niaw.launchnea.DataClasses

import androidx.room.Entity

@Entity(primaryKeys = ["launchID", "ReuseID"])
data class LaunchReuseCrossRef(
    var launchID: String,
    var reuseID: String,
)
