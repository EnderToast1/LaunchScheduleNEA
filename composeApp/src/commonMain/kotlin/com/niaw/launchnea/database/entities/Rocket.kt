package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Agency::class,
            parentColumns = ["agencyID"],
            childColumns = ["agencyID"],
            onDelete = ForeignKey.CASCADE
        )
])
data class Rocket(
    @PrimaryKey(autoGenerate = false)
    var rocketID: String,
    var name: String,
    var agencyID: String,

    var imageURL: String,
    var desc: String, //long

  )