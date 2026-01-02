package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.database.entities.Agency
import com.niaw.launchnea.database.entities.Rocket


data class AgencyWithRockets(
    @Embedded var agency: Agency,
    @Relation(
        parentColumn = "agencyID",
        entityColumn = "agencyID"
    )
    var rockets: List<Rocket>
)
