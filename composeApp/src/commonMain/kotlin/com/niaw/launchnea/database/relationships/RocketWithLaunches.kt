package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Rocket

data class RocketWithLaunches(
    @Embedded var rocket: Rocket,
    @Relation(
        parentColumn = "rocketID",
        entityColumn = "rocketID"
    )
    var launches: List<Launch>
)
