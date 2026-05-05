package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "starred_launches",
    foreignKeys = [
        ForeignKey(
            entity = Launch::class,
            parentColumns = ["launchID"],
            childColumns = ["launchID"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index("launchID")]
)
data class StarredLaunch(
    @PrimaryKey val launchID: String
)
