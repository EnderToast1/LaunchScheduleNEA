package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Agencies
import com.niaw.launchnea.DataClasses.Rockets


data class AgencyWithRockets(
    @Embedded var agencies: Agencies,
    @Relation(
        parentColumn = "agencyID",
        entityColumn = "agencyID"
    )
    var rockets: List<Rockets>
)
