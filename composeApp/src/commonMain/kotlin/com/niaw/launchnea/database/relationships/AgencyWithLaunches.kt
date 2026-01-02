package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Launch

data class AgencyWithLaunches(
    @Embedded var agency: Agency,
    @Relation(
        parentColumn = "agencyID",
        entityColumn = "agencyID"
    )
    var launches: List<Launch>
)