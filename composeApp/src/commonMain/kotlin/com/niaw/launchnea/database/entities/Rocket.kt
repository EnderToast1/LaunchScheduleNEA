package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rocket(
    @PrimaryKey(autoGenerate = false)
    var rocketID: String,
    var name: String,
    var agencyID: String,

    var imageURL: String,
    var desc: String, //long

  )