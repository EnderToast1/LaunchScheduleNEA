package com.niaw.launchnea.Data_Classes

data class Agencies(
  //Database Variables
    var id: String,
    var name: String,
    var launchNumber: Int,

  //Rest of data needed
    var country: String,
    var description: String, //long text
    var leader: String,
    var leaderType: LeaderType = LeaderType.UnknownLeaderType,
    var founded: Int,
    var logoURL: String? = null,
    var websiteURL: String? =null,
    var wikiURL: String? =null,
    var socialsURL: String? =null,
    var youtubeURL: String? =null
  )