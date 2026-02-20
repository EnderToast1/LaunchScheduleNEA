package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MetaData")
data class MetaData(
    @PrimaryKey val data: String, // e.g., "last_update"
    val value: String // store as ISO string
)