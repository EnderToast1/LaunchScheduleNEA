package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse

data class launchWithReuses(
    @Embedded var launch: Launch,
    @Relation(
        parentColumn = "launchID",
        entityColumn = "reuseID",
        associateBy = Junction(LaunchReuseCrossRef::class)
    )
    var reuses: List<Reuse>
)
