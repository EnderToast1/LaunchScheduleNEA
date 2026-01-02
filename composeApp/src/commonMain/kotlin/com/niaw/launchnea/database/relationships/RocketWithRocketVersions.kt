package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Rocket
import com.niaw.launchnea.database.entities.RocketVersion


data class RocketWithRocketVersions(
    @Embedded var rocket: Rocket,
    @Relation(
        parentColumn = "rocketID",
        entityColumn = "rocketID"
    )
    var rocketVersions: List<RocketVersion>
)
