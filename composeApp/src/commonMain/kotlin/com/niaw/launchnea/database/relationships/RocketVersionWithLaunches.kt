package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.RocketVersion

data class RocketVersionWithLaunches(
    @Embedded var rocketVersion: RocketVersion,
    @Relation(
        parentColumn = "rocketVersionID",
        entityColumn = "rocketVersionID"
    )
    var launches: List<Launch>
)
