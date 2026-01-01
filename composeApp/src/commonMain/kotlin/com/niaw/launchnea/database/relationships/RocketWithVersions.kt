package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Rockets
import com.niaw.launchnea.DataClasses.Version


data class RocketWithVersions(
    @Embedded var rocket: Rockets,
    @Relation(
        parentColumn = "rocketID",
        entityColumn = "rocketID"
    )
    var versions: List<Version>
)
