package com.niaw.launchnea.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.niaw.launchnea.DataClasses.Reuse
import com.niaw.launchnea.DataClasses.Version

data class VersionWithReuse(
    @Embedded var version: Version,
    @Relation(
        parentColumn = "id",
        entityColumn = "versionID"
    )
    var reuses: List<Reuse>
)
