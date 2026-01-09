package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.niaw.launchnea.LeaderType

@Entity

data class Agency(
  //Database Variables
    @PrimaryKey(autoGenerate = false)
    var agencyID: String,
    var name: String,
    var launchNumber: Int=0,

  //Rest of data needed
    var country: String,
    var description: String, //long text
    var leader: String,
    var leaderType: LeaderType = LeaderType.UnknownLeaderType,
    var founded: Int=0,
    var logoURL: String? = null,
    var websiteURL: String? =null,
    var wikiURL: String? =null,
    var socialsURL: String? =null,
    var youtubeURL: String? =null
  )