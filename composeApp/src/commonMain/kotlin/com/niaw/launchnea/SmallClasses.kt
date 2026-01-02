package com.niaw.launchnea

import kotlinx.serialization.Serializable

//a set of short classes that are needed
@Serializable
enum class ResultsState {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILURE,
    UNKNOWN
    }

enum class LeaderType(val displayName: String) {
    CEO("CEO"),
    Admin("Admin"),
    UnknownLeaderType("Unknown leader type");
}
@Serializable
enum class ReuseType{
    BOOSTER,
    SHIP,
    CAPSULE,
    UNKNOWN
}

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class Reusable(
    var reuseID: String,
    var flightNumber: Int,
    var landingLocationAbbr: String,
    var landingLocationName: String,
    var reuseType: ReuseType,
    var status: ResultsState
){
       }
