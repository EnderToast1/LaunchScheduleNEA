package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.LaunchReuseCrossRef
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Reuse

data class launchWithReuses(
    @Embedded var launch: Launches,
    @Relation(
        parentColumn = "launchID",
        entityColumn = "reuseID",
        associateBy = Junction(LaunchReuseCrossRef::class)
    )
    var reuses: List<Reuse>
)
