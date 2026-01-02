package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Reuse
import com.niaw.launchnea.database.entities.RocketVersion

data class RocketVersionWithReuse(
    @Embedded var rocketVersion: RocketVersion,
    @Relation(
        parentColumn = "versionID",
        entityColumn = "versionID"
    )
    var reuses: List<Reuse>
)
