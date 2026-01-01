package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Agencies
import com.niaw.launchnea.DataClasses.Launches

data class AgencyWithLaunches(
    @Embedded var agency: Agencies,
    @Relation(
        parentColumn = "agencyID",
        entityColumn = "agencyID"
    )
    var launches: List<Launches>
)