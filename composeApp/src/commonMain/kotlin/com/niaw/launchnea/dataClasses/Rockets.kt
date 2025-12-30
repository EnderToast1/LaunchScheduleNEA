package com.niaw.launchnea.DataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rockets(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var agencyID: String,

    var imageURL: String,
    var desc: String, //long

  )