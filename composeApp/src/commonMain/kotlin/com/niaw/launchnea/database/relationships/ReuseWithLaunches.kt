package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.niaw.launchnea.database.entities.LaunchReuseCrossRef
import com.niaw.launchnea.database.entities.Launch
import com.niaw.launchnea.database.entities.Reuse

data class ReuseWithLaunches(
    @Embedded var resue: Reuse,
    @Relation(
        parentColumn = "reuseID",
        entityColumn = "launchID",
        associateBy = Junction(LaunchReuseCrossRef::class)
    )
    var launches: List<Launch>
)
