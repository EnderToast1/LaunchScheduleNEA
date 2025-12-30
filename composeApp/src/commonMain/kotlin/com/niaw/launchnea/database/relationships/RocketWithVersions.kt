package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Rockets
import jdk.javadoc.internal.Versions

data class RocketWithVersions(
    @Embedded var rocket: Rockets,
    @Relation(
        parentColumn = "id",
        entityColumn = "rocketID"
    )
    var versions: List<Versions>
)
