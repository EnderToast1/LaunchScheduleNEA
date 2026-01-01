package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Version

data class RocketVersionWithLaunches(
    @Embedded var rocketVersion: Version,
    @Relation(
        parentColumn = "rocketVersionID",
        entityColumn = "rocketVersionID"
    )
    var launches: List<Launches>
)
