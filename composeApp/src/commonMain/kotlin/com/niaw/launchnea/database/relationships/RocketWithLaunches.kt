package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Rockets

data class RocketWithLaunches(
    @Embedded var rockets: Rockets,
    @Relation(
        parentColumn = "id",
        entityColumn = "rocketID"
    )
    var launches: List<Launches>
)
