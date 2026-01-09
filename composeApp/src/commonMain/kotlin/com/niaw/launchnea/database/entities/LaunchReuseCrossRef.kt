package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["launchID", "reuseID"],
    foreignKeys = [
        ForeignKey(
            entity = Launch::class,
            parentColumns = ["launchID"],
            childColumns = ["launchID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Reuse::class,
            parentColumns = ["reuseID"],
            childColumns = ["reuseID"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class LaunchReuseCrossRef(
    var launchID: String,
    var reuseID: String,
)
