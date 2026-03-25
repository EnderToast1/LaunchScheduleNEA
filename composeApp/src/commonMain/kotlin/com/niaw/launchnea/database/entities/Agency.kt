package com.niaw.launchnea.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.niaw.launchnea.utils.LeaderType
import java.time.LocalDateTime

@Entity
data class Agency(
  //Database Search Variables
    @PrimaryKey(autoGenerate = false)
    var agencyID: String,
    var name: String? = null,
    var launchNumber: Int=0,

  //Rest of data needed
    var countries: List<String?> = emptyList(),
    var description: String? = null, //long text
    var leader: String? = null,
    var leaderType: LeaderType = LeaderType.UnknownLeaderType,
    var founded: Int=0,
    var logoURL: String? = null,
    var websiteURL: String? =null,
    var wikiURL: String? =null,
    var socialsURL: String? =null,
    var youtubeURL: String? =null,
    var lastUpdated: LocalDateTime?=null,
  )
