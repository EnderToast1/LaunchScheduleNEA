package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.LaunchReuseCrossRef
import com.niaw.launchnea.DataClasses.Launches
import com.niaw.launchnea.DataClasses.Reuse

data class ReuseWithLaunches(
    @Embedded var resue: Reuse,
    @Relation(
        parentColumn = "reuseID",
        entityColumn = "launchID",
        associateBy = Junction(LaunchReuseCrossRef::class)
    )
    var launches: List<Launches>
)
